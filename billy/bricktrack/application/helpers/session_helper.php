<?php
// session_helper.php

// Start the session if it's not already started
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

// Function to set session data with a countdown (in seconds)
function set_userdata($key, $value, $countdown = null) {
    $_SESSION[$key] = [
        'value' => $value,
        'expires' => $countdown !== null ? time() + $countdown : null
    ];
}


// Function to get session data and check expiration if applicable
function userdata($key) {
    if (isset($_SESSION[$key])) {
        $userdata = $_SESSION[$key];
        
        // Check if the data has an expiration time
        if ($userdata['expires'] !== null && time() >= $userdata['expires']) {
            // Data has expired, unset it and return null
            unset($_SESSION[$key]);
            return null;
        }
        
        return $userdata['value'];
    }
    return null;
}


// Function to unset session data
function unset_userdata($key) {
    unset($_SESSION[$key]);
}

// Function to destroy the session
function destroy_session() {
    $_SESSION = array();
    session_destroy();
}

// Function to set flashdata
function set_flashdata($key, $value) {
    $_SESSION['flashdata'][$key] = $value;
}

// Function to get flashdata and then unset it
function get_flashdata($key) {
    if (isset($_SESSION['flashdata'][$key])) {
        $value = $_SESSION['flashdata'][$key];
        unset($_SESSION['flashdata'][$key]);
        return $value;
    }
    return null;
}

// Function to set tempdata with an expiration time (in seconds)
function set_tempdata($key, $value, $countdown) {
    $_SESSION['tempdata'][$key] = [
        'value' => $value,
        'expires' => time() + $countdown
    ];
}

// Function to get tempdata and then unset it if it hasn't expired
function get_tempdata($key) {
    if (isset($_SESSION['tempdata'][$key])) {
        $tempdata = $_SESSION['tempdata'][$key];
        
        // Check if the data has expired
        if (time() < $tempdata['expires']) {
            $value = $tempdata['value'];
            unset($_SESSION['tempdata'][$key]);
            return $value;
        } else {
            // If expired, remove the tempdata
            unset($_SESSION['tempdata'][$key]);
        }
    }
    return null;
}

?>
