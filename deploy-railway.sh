#!/bin/bash

# Quick Railway Deployment Script
# This script deploys to Railway for a public URL

set -e

# Colors for output
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

print_header() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}  Railway Quick Deployment     ${NC}"
    echo -e "${BLUE}================================${NC}"
}

# Check if git is initialized
check_git() {
    if [ ! -d ".git" ]; then
        print_status "Initializing git repository..."
        git init
        git add .
        git commit -m "Initial commit for Railway deployment"
    fi
}

# Deploy to Railway
deploy_railway() {
    print_header
    
    print_status "Preparing for Railway deployment..."
    
    # Check git
    check_git
    
    print_status "Railway configuration created!"
    print_warning "To deploy to Railway:"
    echo
    echo "1. Go to https://railway.app"
    echo "2. Sign up/Login with GitHub"
    echo "3. Click 'New Project'"
    echo "4. Choose 'Deploy from GitHub repo'"
    echo "5. Select this repository: qlns_philong"
    echo "6. Railway will automatically detect the configuration"
    echo "7. Set environment variables in Railway dashboard:"
    echo "   - DATABASE_URL: sqlite:///./philong.db"
    echo "   - SECRET_KEY: a8f0e55e8601a369c47713448e0493889761a7e6c859e752aba8e21e0af3be2e"
    echo "   - DEBUG: False"
    echo "   - ENVIRONMENT: production"
    echo
    print_status "Railway will provide a public URL like:"
    echo "https://your-app-name.railway.app"
    echo
    print_status "API endpoints will be available at:"
    echo "https://your-app-name.railway.app/docs"
    echo "https://your-app-name.railway.app/health"
    echo "https://your-app-name.railway.app/api/employees/"
}

# Alternative: Deploy to Render
deploy_render() {
    print_status "Preparing for Render deployment..."
    
    # Create render.yaml
    cat > render.yaml << EOF
services:
  - type: web
    name: philong-backend
    env: python
    buildCommand: pip install -r backend/requirements.txt
    startCommand: cd backend && uvicorn app.main:app --host 0.0.0.0 --port \$PORT
    envVars:
      - key: DATABASE_URL
        value: sqlite:///./philong.db
      - key: SECRET_KEY
        generateValue: true
      - key: DEBUG
        value: False
      - key: ENVIRONMENT
        value: production
EOF
    
    print_status "Render configuration created!"
    print_warning "To deploy to Render:"
    echo
    echo "1. Go to https://render.com"
    echo "2. Sign up/Login with GitHub"
    echo "3. Click 'New +' -> 'Web Service'"
    echo "4. Connect your GitHub repository"
    echo "5. Select this repository: qlns_philong"
    echo "6. Render will use the render.yaml configuration"
    echo "7. Click 'Create Web Service'"
    echo
    print_status "Render will provide a public URL like:"
    echo "https://your-app-name.onrender.com"
}

# Main function
main() {
    echo "Choose deployment platform:"
    echo "1. Railway (recommended - faster)"
    echo "2. Render (alternative)"
    echo
    read -p "Enter your choice (1-2): " choice
    
    case $choice in
        1)
            deploy_railway
            ;;
        2)
            deploy_render
            ;;
        *)
            print_warning "Invalid choice, using Railway..."
            deploy_railway
            ;;
    esac
    
    echo
    print_status "Deployment configuration ready!"
    print_status "Follow the steps above to get your public URL."
}

# Run main function
main "$@" 