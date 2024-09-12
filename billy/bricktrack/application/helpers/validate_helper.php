<?php defined('BASEPATH') or exit('No direct script access allowed');

if (!function_exists('validate_input')) {
    function validate_input($required_fields, $flash_key, $class, $error_message = 'All inputs are required.')
    {
        $CI =& get_instance();
        $CI->load->library('form_validation');

        $modal = '$("#' . $class . ' ").modal("show")';

        // Sanitize and validate each required field
        foreach ($required_fields as $field) {
            if (empty(xss_clean($field))) {
                // Set flash message for empty inputs
                set_flashdata($flash_key, $error_message);
                set_flashdata('modal', $modal);
                $redirect_url = isset($_SERVER['HTTP_REFERER']) ? $_SERVER['HTTP_REFERER'] : '/';
                redirect($redirect_url);
                return false; // Indicate validation failure
            }
        }

        return true; // Indicate validation success
    }
}

if (!function_exists('validate_form')) {
    function validate_form($rules, $flash_key, $error_message = 'All inputs are required.')
    {
        $CI =& get_instance();
        $CI->load->library('form_validation');

        foreach ($rules as $field => $rule) {
            $CI->form_validation->set_rules($field, $rule['field'], $rule['rules']);
        }

        if ($CI->form_validation->run() == FALSE) {
            // Set flash message for validation errors
            set_flashdata($flash_key, $error_message);
            // Redirect back to the referrer URL
            $redirect_url = isset($_SERVER['HTTP_REFERER']) ? $_SERVER['HTTP_REFERER'] : '/';
            redirect($redirect_url);
            return false;
        }

        return true;
    }
}
