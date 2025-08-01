import os
import uuid
from pathlib import Path
from typing import Optional
from PIL import Image
import aiofiles
from fastapi import UploadFile, HTTPException
import logging

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Configuration
UPLOAD_DIR = Path("uploads")
AVATAR_DIR = UPLOAD_DIR / "avatars"
ID_CARD_DIR = UPLOAD_DIR / "id_cards"

# Create directories if they don't exist
UPLOAD_DIR.mkdir(exist_ok=True)
AVATAR_DIR.mkdir(exist_ok=True)
ID_CARD_DIR.mkdir(exist_ok=True)

# Allowed image types
ALLOWED_IMAGE_TYPES = {
    "image/jpeg",
    "image/jpg", 
    "image/png",
    "image/webp"
}

# Maximum file size (5MB)
MAX_FILE_SIZE = 5 * 1024 * 1024

def validate_image_file(file: UploadFile) -> bool:
    """Validate uploaded image file"""
    if not file.content_type in ALLOWED_IMAGE_TYPES:
        raise HTTPException(
            status_code=400, 
            detail=f"File type not allowed. Allowed types: {', '.join(ALLOWED_IMAGE_TYPES)}"
        )
    
    if file.size and file.size > MAX_FILE_SIZE:
        raise HTTPException(
            status_code=400,
            detail=f"File too large. Maximum size: {MAX_FILE_SIZE // (1024*1024)}MB"
        )
    
    return True

def generate_unique_filename(original_filename: str, prefix: str = "") -> str:
    """Generate unique filename with UUID"""
    # Get file extension
    file_ext = Path(original_filename).suffix.lower()
    
    # Generate unique ID
    unique_id = str(uuid.uuid4())
    
    # Create filename
    if prefix:
        filename = f"{prefix}_{unique_id}{file_ext}"
    else:
        filename = f"{unique_id}{file_ext}"
    
    return filename

async def save_uploaded_image(
    file: UploadFile, 
    upload_dir: Path,
    filename: Optional[str] = None,
    max_size: tuple = (800, 800),
    quality: int = 85
) -> str:
    """Save uploaded image with optimization"""
    
    # Validate file
    validate_image_file(file)
    
    # Generate filename if not provided
    if not filename:
        filename = generate_unique_filename(file.filename)
    
    # Full path for saving
    file_path = upload_dir / filename
    
    try:
        # Read file content
        content = await file.read()
        
        # Open image with PIL
        with Image.open(io.BytesIO(content)) as img:
            # Convert to RGB if necessary
            if img.mode in ('RGBA', 'LA', 'P'):
                img = img.convert('RGB')
            
            # Resize if larger than max_size
            if img.size[0] > max_size[0] or img.size[1] > max_size[1]:
                img.thumbnail(max_size, Image.Resampling.LANCZOS)
            
            # Save optimized image
            img.save(file_path, 'JPEG', quality=quality, optimize=True)
        
        logger.info(f"Image saved successfully: {file_path}")
        return str(file_path)
        
    except Exception as e:
        logger.error(f"Error saving image: {str(e)}")
        raise HTTPException(status_code=500, detail=f"Error saving image: {str(e)}")

async def save_avatar_image(file: UploadFile) -> str:
    """Save avatar image with specific settings"""
    filename = generate_unique_filename(file.filename, "avatar")
    return await save_uploaded_image(
        file=file,
        upload_dir=AVATAR_DIR,
        filename=filename,
        max_size=(400, 400),  # Smaller size for avatars
        quality=80
    )

async def save_id_card_image(file: UploadFile) -> str:
    """Save ID card image with specific settings"""
    filename = generate_unique_filename(file.filename, "idcard")
    return await save_uploaded_image(
        file=file,
        upload_dir=ID_CARD_DIR,
        filename=filename,
        max_size=(1200, 800),  # Larger size for ID cards
        quality=90
    )

def get_image_url(file_path: str) -> str:
    """Generate URL for image file"""
    if not file_path:
        return ""
    
    # For production, you might want to use a CDN or cloud storage
    # For now, return relative path
    return f"/uploads/{file_path}"

async def delete_image_file(file_path: str) -> bool:
    """Delete image file from storage"""
    try:
        if file_path and os.path.exists(file_path):
            os.remove(file_path)
            logger.info(f"Image deleted: {file_path}")
            return True
        return False
    except Exception as e:
        logger.error(f"Error deleting image: {str(e)}")
        return False

# Import io for BytesIO 