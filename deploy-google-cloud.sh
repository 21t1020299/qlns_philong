#!/bin/bash

# Google Cloud Deployment Script for PhiLong Backend
# This script deploys the backend to Google Cloud Run

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}  Google Cloud Deployment      ${NC}"
    echo -e "${BLUE}================================${NC}"
}

# Check if gcloud is installed
check_gcloud() {
    if ! command -v gcloud &> /dev/null; then
        print_error "Google Cloud CLI is not installed."
        print_warning "Please install it first:"
        echo "1. Visit: https://cloud.google.com/sdk/docs/install"
        echo "2. Or run: brew install --cask google-cloud-sdk"
        exit 1
    fi
    print_status "Google Cloud CLI found: $(gcloud --version | head -n 1)"
}

# Check if Docker is running
check_docker() {
    if ! docker info &> /dev/null; then
        print_error "Docker is not running."
        print_warning "Please start Docker Desktop first."
        exit 1
    fi
    print_status "Docker is running"
}

# Set up Google Cloud project
setup_project() {
    print_status "Setting up Google Cloud project..."
    
    # Get current project
    CURRENT_PROJECT=$(gcloud config get-value project 2>/dev/null || echo "")
    
    if [ -z "$CURRENT_PROJECT" ]; then
        print_warning "No Google Cloud project is set."
        echo "Available projects:"
        gcloud projects list --format="table(projectId,name)"
        echo
        read -p "Enter your Google Cloud project ID: " PROJECT_ID
        gcloud config set project $PROJECT_ID
    else
        print_status "Using project: $CURRENT_PROJECT"
        PROJECT_ID=$CURRENT_PROJECT
    fi
    
    # Enable required APIs
    print_status "Enabling required APIs..."
    gcloud services enable cloudbuild.googleapis.com
    gcloud services enable run.googleapis.com
    gcloud services enable containerregistry.googleapis.com
    
    print_status "Project setup completed!"
}

# Build and push Docker image
build_and_push() {
    print_status "Building and pushing Docker image..."
    
    # Get project ID
    PROJECT_ID=$(gcloud config get-value project)
    
    # Build and tag image
    docker build -t gcr.io/$PROJECT_ID/philong-backend:latest backend/
    
    # Configure Docker to use gcloud as a credential helper
    gcloud auth configure-docker
    
    # Push image to Google Container Registry
    docker push gcr.io/$PROJECT_ID/philong-backend:latest
    
    print_status "Docker image pushed successfully!"
}

# Deploy to Cloud Run
deploy_to_cloud_run() {
    print_status "Deploying to Google Cloud Run..."
    
    # Get project ID
    PROJECT_ID=$(gcloud config get-value project)
    
    # Deploy to Cloud Run
    gcloud run deploy philong-backend \
        --image gcr.io/$PROJECT_ID/philong-backend:latest \
        --platform managed \
        --region us-central1 \
        --allow-unauthenticated \
        --port 8000 \
        --memory 512Mi \
        --cpu 1 \
        --max-instances 10 \
        --set-env-vars="DATABASE_URL=sqlite:///./philong.db,SECRET_KEY=a8f0e55e8601a369c47713448e0493889761a7e6c859e752aba8e21e0af3be2e,DEBUG=False,ENVIRONMENT=production"
    
    print_status "Deployment completed!"
    
    # Get the service URL
    SERVICE_URL=$(gcloud run services describe philong-backend --platform managed --region us-central1 --format="value(status.url)")
    print_status "Service URL: $SERVICE_URL"
    print_status "API Docs: $SERVICE_URL/docs"
}

# Deploy to App Engine (alternative)
deploy_to_app_engine() {
    print_status "Deploying to Google App Engine..."
    
    # Create app.yaml if it doesn't exist
    if [ ! -f "backend/app.yaml" ]; then
        cat > backend/app.yaml << EOF
runtime: python311
entrypoint: uvicorn app.main:app --host 0.0.0.0 --port \$PORT

env_variables:
  DATABASE_URL: "sqlite:///./philong.db"
  SECRET_KEY: "a8f0e55e8601a369c47713448e0493889761a7e6c859e752aba8e21e0af3be2e"
  DEBUG: "False"
  ENVIRONMENT: "production"

automatic_scaling:
  target_cpu_utilization: 0.6
  min_instances: 1
  max_instances: 10

resources:
  cpu: 1
  memory_gb: 0.5
  disk_size_gb: 10
EOF
    fi
    
    # Deploy to App Engine
    cd backend
    gcloud app deploy app.yaml --quiet
    
    print_status "App Engine deployment completed!"
    
    # Get the service URL
    SERVICE_URL=$(gcloud app browse --no-launch-browser)
    print_status "Service URL: $SERVICE_URL"
    print_status "API Docs: $SERVICE_URL/docs"
}

# Test deployment
test_deployment() {
    print_status "Testing deployment..."
    
    # Get the service URL
    if [ "$DEPLOYMENT_TYPE" = "cloudrun" ]; then
        SERVICE_URL=$(gcloud run services describe philong-backend --platform managed --region us-central1 --format="value(status.url)")
    else
        SERVICE_URL=$(gcloud app browse --no-launch-browser)
    fi
    
    print_status "Testing health endpoint..."
    curl -s "$SERVICE_URL/health" | jq . || echo "Health check failed"
    
    print_status "Testing API endpoint..."
    curl -s "$SERVICE_URL/api/employees/" | jq . || echo "API test failed"
    
    print_status "Testing completed!"
}

# Main deployment function
main() {
    print_header
    
    # Check prerequisites
    check_gcloud
    check_docker
    
    # Setup project
    setup_project
    
    # Choose deployment method
    echo "Choose deployment method:"
    echo "1. Google Cloud Run (recommended)"
    echo "2. Google App Engine"
    echo
    read -p "Enter your choice (1-2): " choice
    
    case $choice in
        1)
            DEPLOYMENT_TYPE="cloudrun"
            build_and_push
            deploy_to_cloud_run
            ;;
        2)
            DEPLOYMENT_TYPE="appengine"
            deploy_to_app_engine
            ;;
        *)
            print_error "Invalid choice"
            exit 1
            ;;
    esac
    
    # Test deployment
    test_deployment
    
    echo
    print_status "Deployment completed successfully!"
    echo
    print_status "Next steps:"
    echo "1. Set up your database (Supabase recommended)"
    echo "2. Update environment variables in Google Cloud Console"
    echo "3. Configure custom domain (optional)"
    echo "4. Set up monitoring and logging"
}

# Run main function
main "$@"