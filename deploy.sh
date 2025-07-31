#!/bin/bash

echo "🚀 Starting deployment process..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if git is installed
if ! command -v git &> /dev/null; then
    print_error "Git is not installed. Please install git first."
    exit 1
fi

# Check if we're in a git repository
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    print_error "Not in a git repository. Please initialize git first."
    exit 1
fi

# Check current branch
CURRENT_BRANCH=$(git branch --show-current)
print_status "Current branch: $CURRENT_BRANCH"

# Check for uncommitted changes
if ! git diff-index --quiet HEAD --; then
    print_warning "You have uncommitted changes. Do you want to commit them? (y/n)"
    read -r response
    if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
        print_status "Committing changes..."
        git add .
        git commit -m "Auto-commit before deployment"
    else
        print_error "Please commit or stash your changes before deploying."
        exit 1
    fi
fi

# Push to remote repository
print_status "Pushing to remote repository..."
if git push origin "$CURRENT_BRANCH"; then
    print_status "✅ Code pushed successfully!"
else
    print_error "Failed to push code to remote repository."
    exit 1
fi

print_status "🎉 Deployment initiated!"
print_status "Backend will be deployed to: https://qlns-philong.onrender.com"
print_status "Frontend will be deployed to: https://qlns-philong.vercel.app"
print_status ""
print_status "Please check the deployment status in your dashboard:"
print_status "- Render: https://dashboard.render.com"
print_status "- Vercel: https://vercel.com/dashboard" 