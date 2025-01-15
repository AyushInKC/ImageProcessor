Image Processing API Documentation
Overview
This API provides a set of endpoints for image upload, retrieval, and various image processing functionalities such as resizing, rotating, cropping, filtering, and background removal.
Table of Contents
1. Image Upload and Retrieval
2. Image Manipulation
3. Background Removal
4. User Authentication
5. Error Handling
Image Upload and Retrieval
POST /img/uploadImg
Uploads an image file.
Request Body: file (MultipartFile)
Response: 200 OK - "File uploaded successfully: [fileId]"
GET /img/getImg/{id}
Retrieves an image by its ID.
Parameters: id (String)
Response: 200 OK - Image data in its original format.
GET /img/getImgData/{id}
Retrieves metadata for an image by its ID.
Parameters: id (String)
Response: 200 OK - Metadata (e.g., name, content type).
DELETE /img/deleteImg/{id}
Deletes an image by its ID.
Parameters: id (String)
Response: 200 OK - "Image deleted Successfully"
PUT /img/updateImgData/{id}
Updates the metadata of an image.
Parameters: id (String), name (String), contentType (String)
Response: 200 OK - Updated metadata.
PUT /img/updateImgFile/{id}
Updates the image file for a given ID.
Parameters: id (String), file (MultipartFile)
Response: 200 OK - "Image updated successfully" or 404 Not Found if the image doesn't exist.
Image Manipulation
POST /img/resizeImg
Resizes an image.
Request Body: file (MultipartFile), width (int), height (int)
Response: 200 OK - Resized image data.
POST /img/rotateImg
Rotates an image by the specified angle.
Request Body: file (MultipartFile), angle (double)
Response: 200 OK - Rotated image data.
POST /img/cropImg
Crops an image.
Request Body: file (MultipartFile), x (int), y (int), width (int), height (int)
Response: 200 OK - Cropped image data.
POST /img/addWaterMark
Adds a watermark to an image.
Request Body: file (MultipartFile), title (String)
Response: 200 OK - Watermarked image data.
POST /img/convertToJPG
Converts an image to JPG format.
Request Body: file (MultipartFile)
Response: 200 OK - JPG image data.
POST /img/convertImageToPDF
Converts an image to a PDF document.
Request Body: file (MultipartFile)
Response: 200 OK - PDF document containing the image.
POST /img/addGrayScaleFilter
Adds a grayscale filter to an image.
Request Body: file (MultipartFile)
Response: 200 OK - Grayscale image data.
POST /img/addSepiaFilter
Adds a sepia filter to an image.
Request Body: file (MultipartFile)
Response: 200 OK - Sepia image data.
POST /img/mirrorImage
Mirrors an image in the specified direction.
Request Body: file (MultipartFile), direction (String: "horizontal" or "vertical")
Response: 200 OK - Mirrored image data.
POST /img/compressImage
Compresses an image to a specified size.
Request Body: file (MultipartFile), size (float)
Response: 200 OK - Compressed image data.
POST /img/blurImage
Adds a blur effect to an image.
Request Body: file (MultipartFile)
Response: 200 OK - Blurred image data.
POST /img/tintImage
Adds a tint to an image.
Request Body: file (MultipartFile), r, g, b (String: Red, Green, Blue values)
Response: 200 OK - Tinted image data.
Background Removal
POST /img/remove-background
Removes the background from an image.
Request Body: file (MultipartFile)
Response: 200 OK - Image with the background removed.
User Authentication
POST /auth/register
Registers a new user.
Request Body: username (String), password (String)
Response: 201 Created - User entity or conflict message if the username exists.
POST /auth/login
Logs a user in and returns a JWT token.
Request Body: username (String), password (String)
Response: 200 OK - JWT token on successful login.
401 Unauthorized - Invalid credentials.
Error Handling
400 Bad Request - Invalid input or missing parameters.
401 Unauthorized - Invalid credentials during login.
404 Not Found - Resource not found.
500 Internal Server Error - Server error.
