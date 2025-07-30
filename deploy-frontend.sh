#!/bin/bash

# Deploy Frontend to Render
echo "🚀 Deploying Frontend to Render..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Check if we're in the right directory
if [ ! -f "package.json" ]; then
    echo -e "${RED}❌ Error: package.json not found. Please run this script from the frontend directory.${NC}"
    exit 1
fi

echo -e "${BLUE}📦 Building React app...${NC}"

# Clean and install dependencies
echo -e "${YELLOW}🧹 Cleaning previous build...${NC}"
rm -rf node_modules package-lock.json build

echo -e "${YELLOW}📥 Installing dependencies...${NC}"
npm install

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Error: Failed to install dependencies${NC}"
    exit 1
fi

# Build the app
echo -e "${YELLOW}🔨 Building production version...${NC}"
npm run build

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Error: Build failed${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Build completed successfully!${NC}"

# Check if build directory exists
if [ ! -d "build" ]; then
    echo -e "${RED}❌ Error: Build directory not found${NC}"
    exit 1
fi

echo -e "${BLUE}📋 Build Summary:${NC}"
echo -e "${YELLOW}📁 Build directory:${NC} $(pwd)/build"
echo -e "${YELLOW}📊 Build size:${NC} $(du -sh build | cut -f1)"

# Create static.json for Render (if needed)
if [ ! -f "static.json" ]; then
    echo -e "${YELLOW}📝 Creating static.json for Render...${NC}"
    cat > static.json << EOF
{
  "root": "build",
  "clean_urls": true,
  "routes": {
    "/**": "index.html"
  },
  "https_only": true,
  "headers": {
    "/static/**": {
      "Cache-Control": "public, max-age=31536000"
    }
  }
}
EOF
fi

echo -e "${GREEN}🎉 Frontend is ready for deployment!${NC}"
echo -e "${BLUE}📋 Next steps:${NC}"
echo -e "${YELLOW}1.${NC} Go to Render Dashboard: https://dashboard.render.com"
echo -e "${YELLOW}2.${NC} Create new Static Site"
echo -e "${YELLOW}3.${NC} Connect your GitHub repository"
echo -e "${YELLOW}4.${NC} Set build command: ${GREEN}cd frontend && npm install && npm run build${NC}"
echo -e "${YELLOW}5.${NC} Set publish directory: ${GREEN}frontend/build${NC}"
echo -e "${YELLOW}6.${NC} Add environment variable: ${GREEN}REACT_APP_API_URL=https://qlns-philong.onrender.com/api${NC}"

echo -e "${BLUE}🔗 Or use the quick deploy command:${NC}"
echo -e "${GREEN}render deploy --static-dir frontend/build${NC}"

echo -e "${GREEN}✅ Frontend deployment script completed!${NC}" 