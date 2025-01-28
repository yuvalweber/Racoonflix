const multer = require('multer');
const path = require('path');

// Configure storage
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        if (file.fieldname === 'image' || file.fieldname === 'profilePicture') {
            cb(null, 'uploads/images'); // Path for image uploads
        } else if (file.fieldname === 'trailer') {
            cb(null, 'uploads/movies'); // Path for trailer uploads
        }
    },
    filename: (req, file, cb) => {
        cb(null, `${Date.now()}_${file.originalname}`);
    }
});

// File filter
const fileFilter = (req, file, cb) => {
    if (file.fieldname === 'image' || file.fieldname === 'profilePicture') {
        // Accept only image files
        if (file.mimetype.startsWith('image/')) {
            cb(null, true);
        } else {
            cb(new Error('Invalid file type for image'), false);
        }
    } else if (file.fieldname === 'trailer') {
        // Accept only video files
        if (file.mimetype.startsWith('video/')) {
            cb(null, true);
        } else {
            cb(new Error('Invalid file type for trailer'), false);
        }
    }
};

const upload = multer({
    storage,
    fileFilter,
    limits: { fileSize: 1024 * 1024 * 50 } // Limit file size to 50MB
});

module.exports = upload;
