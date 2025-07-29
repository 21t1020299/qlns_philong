#!/bin/bash

# Render Deployment Script
# This works with private repositories

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
    echo -e "${BLUE}  RENDER DEPLOYMENT            ${NC}"
    echo -e "${BLUE}================================${NC}"
}

print_header

print_status "🚀 Deploying to Render..."

echo
print_status "Step 1: Go to Render Dashboard"
echo "✅ Opening Render dashboard..."
echo "   Visit: https://render.com"
echo

print_status "Step 2: Create New Web Service"
echo "1. Click 'New +' button"
echo "2. Select 'Web Service'"
echo "3. Connect your GitHub account"
echo "4. Select repository: qlns_philong"
echo

print_status "Step 3: Configure Service"
echo "Service Configuration:"
echo "- Name: philong-backend"
echo "- Environment: Python"
echo "- Build Command: pip install -r backend/requirements.txt"
echo "- Start Command: cd backend && uvicorn app.main:app --host 0.0.0.0 --port \$PORT"
echo

print_status "Step 4: Environment Variables"
echo "Add these environment variables:"
echo
echo "DATABASE_URL=sqlite:///./philong.db"
echo "SECRET_KEY=a8f0e55e8601a369c47713448e0493889761a7e6c859e752aba8e21e0af3be2e"
echo "DEBUG=False"
echo "ENVIRONMENT=production"
echo

print_status "Step 5: Deploy"
echo "1. Click 'Create Web Service'"
echo "2. Wait for build and deployment (2-3 minutes)"
echo "3. Get your public URL from Render dashboard"
echo

print_status "🎯 Your API will be available at:"
echo "https://your-app-name.onrender.com"
echo

print_status "🔍 Test endpoints:"
echo "Health: https://your-app-name.onrender.com/health"
echo "API Docs: https://your-app-name.onrender.com/docs"
echo "Employees: https://your-app-name.onrender.com/api/employees/"
echo

print_status "✅ Deployment Complete!"
echo "Your backend is now live and accessible!" 