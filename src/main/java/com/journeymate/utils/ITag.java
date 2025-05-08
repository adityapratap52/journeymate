package com.journeymate.utils;

public interface ITag {
    // Common descriptions
    String API_RESPONSE_200 = "Operation successful";
    String API_RESPONSE_201 = "Resource created successfully";
    String API_RESPONSE_400 = "Bad request";
    String API_RESPONSE_401 = "Unauthorized access";
    String API_RESPONSE_403 = "Access forbidden";
    String API_RESPONSE_404 = "Resource not found";
    
    // Auth Controller
    String AUTH_TAG_NAME = "Authentication";
    String AUTH_TAG_DESCRIPTION = "Authentication management APIs";
    String AUTH_LOGIN_SUMMARY = "Login user";
    String AUTH_LOGIN_DESCRIPTION = "Authenticates a user and returns a JWT token";
    String AUTH_LOGIN_200_DESCRIPTION = "Authentication successful";
    String AUTH_LOGIN_401_DESCRIPTION = "Invalid credentials";
    String AUTH_REGISTER_SUMMARY = "Register new user";
    String AUTH_REGISTER_DESCRIPTION = "Creates a new user account";
    String AUTH_REGISTER_200_DESCRIPTION = "User registered successfully";
    String AUTH_REGISTER_400_DESCRIPTION = "Username or email already exists";
    
    // User Controller
    String USER_TAG_NAME = "User Management";
    String USER_TAG_DESCRIPTION = "User management operations";
    String USER_GET_ALL_SUMMARY = "Get all users";
    String USER_GET_ALL_DESCRIPTION = "Retrieves a list of all users (Admin only)";
    String USER_GET_BY_ID_SUMMARY = "Get user by ID";
    String USER_GET_BY_ID_DESCRIPTION = "Retrieves a specific user by their ID";
    String USER_UPDATE_SUMMARY = "Update user";
    String USER_UPDATE_DESCRIPTION = "Updates an existing user's information";
    String USER_DELETE_SUMMARY = "Delete user";
    String USER_DELETE_DESCRIPTION = "Deletes a user (Admin only)";
    
    // Security Scheme
    String SECURITY_SCHEME_NAME = "bearerAuth";
    String SECURITY_SCHEME_DESCRIPTION = "JWT authentication";
    String SECURITY_SCHEME_BEARER_FORMAT = "JWT";
}