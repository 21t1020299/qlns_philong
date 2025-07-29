#!/bin/bash

# Immediate Deployment Script
# This script provides step-by-step deployment instructions

set -e

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
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
    echo -e "${BLUE}  IMMEDIATE DEPLOYMENT         ${NC}"
    echo -e "${BLUE}================================${NC}"
}

print_header

print_status "🚀 Starting immediate deployment to Railway..."

echo
print_status "Step 1: Railway Dashboard"
echo "✅ Railway dashboard should be open in your browser"
echo "   If not, visit: https://railway.app"
echo

read -p "Press Enter when you're on Railway dashboard..."

print_status "Step 2: Create New Project"
echo "1. Click 'New Project' button"
echo "2. Select 'Deploy from GitHub repo'"
echo "3. Choose repository: qlns_philong"
echo "4. Click 'Deploy Now'"
echo

read -p "Press Enter when project is created..."

print_status "Step 3: Configure Environment Variables"
echo "In Railway dashboard, go to your project and:"
echo "1. Click on 'Variables' tab"
echo "2. Add these environment variables:"
echo
echo "   DATABASE_URL=sqlite:///./philong.db"
echo "   SECRET_KEY=a8f0e55e8601a369c47713448e0493889761a7e6c859e752aba8e21e0af3be2e"
echo "   DEBUG=False"
echo "   ENVIRONMENT=production"
echo

read -p "Press Enter when environment variables are set..."

print_status "Step 4: Wait for Deployment"
echo "⏳ Railway is building and deploying your app..."
echo "   This usually takes 2-3 minutes"
echo "   Watch the deployment logs in Railway dashboard"
echo

print_status "Step 5: Get Your Public URL"
echo "🎯 Once deployment is complete:"
echo "1. Go to 'Settings' tab in Railway"
echo "2. Copy the 'Domain' URL"
echo "3. Your API will be available at:"
echo "   https://your-app-name.railway.app"
echo

print_status "Step 6: Test Your API"
echo "🔍 Test these endpoints:"
echo
echo "Health Check:"
echo "curl https://your-app-name.railway.app/health"
echo
echo "API Info:"
echo "curl https://your-app-name.railway.app/"
echo
echo "Employee List:"
echo "curl https://your-app-name.railway.app/api/employees/"
echo
echo "API Documentation:"
echo "https://your-app-name.railway.app/docs"
echo

print_status "🎉 Deployment Complete!"
echo
echo "Your backend is now live and accessible from anywhere!"
echo "Share the URL with others to test your API."
echo
echo "Next steps:"
echo "1. Test all endpoints"
echo "2. Deploy frontend to Vercel/Netlify"
echo "3. Update frontend API URL"
echo "4. Start using your application!" 