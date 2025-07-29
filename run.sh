#!/bin/bash

# PhiLong Employee Management System - Run Script
# Usage: ./run.sh [command]

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
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

print_header() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}  PhiLong Employee Management  ${NC}"
    echo -e "${BLUE}================================${NC}"
}

# Check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check prerequisites
check_prerequisites() {
    print_status "Checking prerequisites..."
    
    if ! command_exists python3; then
        print_error "Python 3 is not installed"
        exit 1
    fi
    
    if ! command_exists node; then
        print_error "Node.js is not installed"
        exit 1
    fi
    
    if ! command_exists npm; then
        print_error "npm is not installed"
        exit 1
    fi
    
    if ! command_exists docker; then
        print_warning "Docker is not installed. Some commands may not work."
    fi
    
    if ! command_exists docker-compose; then
        print_warning "Docker Compose is not installed. Some commands may not work."
    fi
    
    print_status "Prerequisites check completed"
}

# Install backend dependencies
install_backend() {
    print_status "Installing backend dependencies..."
    cd backend
    
    if [ ! -d "venv" ]; then
        print_status "Creating virtual environment..."
        python3 -m venv venv
    fi
    
    source venv/bin/activate
    pip install -r requirements.txt
    cd ..
    print_status "Backend dependencies installed"
}

# Install frontend dependencies
install_frontend() {
    print_status "Installing frontend dependencies..."
    cd frontend
    npm install
    cd ..
    print_status "Frontend dependencies installed"
}

# Start backend server
start_backend() {
    print_status "Starting backend server..."
    cd backend
    source venv/bin/activate
    uvicorn app.main:app --reload --host 0.0.0.0 --port 8000 &
    cd ..
    print_status "Backend server started on https://qlns-philong.onrender.com"
}

# Start frontend server
start_frontend() {
    print_status "Starting frontend server..."
    cd frontend
    npm start &
    cd ..
    print_status "Frontend server started on http://localhost:3000"
}

# Start with Docker
start_docker() {
    print_status "Starting with Docker..."
    docker-compose up -d
    print_status "Services started with Docker"
    print_status "Frontend: http://localhost:3000"
    print_status "Backend: https://qlns-philong.onrender.com"
    print_status "API Docs: https://qlns-philong.onrender.com/docs"
}

# Stop Docker services
stop_docker() {
    print_status "Stopping Docker services..."
    docker-compose down
    print_status "Docker services stopped"
}

# Build Docker images
build_docker() {
    print_status "Building Docker images..."
    docker-compose build
    print_status "Docker images built"
}

# Show logs
show_logs() {
    print_status "Showing logs..."
    docker-compose logs -f
}

# Clean up
cleanup() {
    print_status "Cleaning up..."
    
    # Stop any running processes
    pkill -f "uvicorn" 2>/dev/null || true
    pkill -f "npm start" 2>/dev/null || true
    
    # Remove virtual environment
    if [ -d "backend/venv" ]; then
        rm -rf backend/venv
        print_status "Virtual environment removed"
    fi
    
    # Remove node_modules
    if [ -d "frontend/node_modules" ]; then
        rm -rf frontend/node_modules
        print_status "node_modules removed"
    fi
    
    # Stop and remove Docker containers
    if command_exists docker-compose; then
        docker-compose down -v 2>/dev/null || true
        print_status "Docker containers and volumes removed"
    fi
    
    print_status "Cleanup completed"
}

# Show status
show_status() {
    print_status "Checking service status..."
    
    # Check backend
    if curl -s http://localhost:8000/health >/dev/null 2>&1; then
        print_status "Backend: Running (http://localhost:8000)"
    else
        print_warning "Backend: Not running"
    fi
    
    # Check frontend
    if curl -s http://localhost:3000 >/dev/null 2>&1; then
        print_status "Frontend: Running (http://localhost:3000)"
    else
        print_warning "Frontend: Not running"
    fi
    
    # Check Docker containers
    if command_exists docker; then
        if docker ps --filter "name=philong" --format "table {{.Names}}\t{{.Status}}" 2>/dev/null | grep -q philong; then
            print_status "Docker containers:"
            docker ps --filter "name=philong" --format "table {{.Names}}\t{{.Status}}"
        else
            print_warning "No Docker containers running"
        fi
    fi
}

# Show help
show_help() {
    print_header
    echo "Usage: ./run.sh [command]"
    echo ""
    echo "Commands:"
    echo "  install     Install all dependencies"
    echo "  start       Start all services (manual mode)"
    echo "  docker      Start with Docker"
    echo "  stop        Stop all services"
    echo "  build       Build Docker images"
    echo "  logs        Show Docker logs"
    echo "  status      Show service status"
    echo "  cleanup     Clean up all files and containers"
    echo "  help        Show this help message"
    echo ""
    echo "Examples:"
    echo "  ./run.sh install    # Install dependencies"
    echo "  ./run.sh docker     # Start with Docker"
    echo "  ./run.sh status     # Check status"
}

# Main script logic
case "${1:-help}" in
    "install")
        check_prerequisites
        install_backend
        install_frontend
        print_status "Installation completed"
        ;;
    "start")
        check_prerequisites
        start_backend
        sleep 2
        start_frontend
        print_status "All services started"
        print_status "Frontend: http://localhost:3000"
        print_status "Backend: https://qlns-philong.onrender.com"
        print_status "API Docs: https://qlns-philong.onrender.com/docs"
        print_status "Press Ctrl+C to stop"
        wait
        ;;
    "docker")
        check_prerequisites
        start_docker
        ;;
    "stop")
        stop_docker
        pkill -f "uvicorn" 2>/dev/null || true
        pkill -f "npm start" 2>/dev/null || true
        print_status "All services stopped"
        ;;
    "build")
        build_docker
        ;;
    "logs")
        show_logs
        ;;
    "status")
        show_status
        ;;
    "cleanup")
        cleanup
        ;;
    "help"|*)
        show_help
        ;;
esac