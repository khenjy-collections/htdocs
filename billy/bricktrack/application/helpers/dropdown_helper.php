<?php
defined('BASEPATH') or exit('No direct script access allowed');

if (!function_exists('dropdown_menu')) {
    function dropdown_menu($tabel, $place)
    {
        // Get CodeIgniter instance
        $CI =& get_instance();
        // Fetch the view variables
        $data = $CI->load->get_vars();

        $alias = lang($tabel . '_alias');
        $url = site_url($data['language'] . '/' . $data[$tabel] . $place);

        return <<<HTML
        <a class="dropdown-item" href="{$url}">
            {$alias}
        </a>
        HTML;
    }
}

if (!function_exists('dropdown_menu_unique')) {
    function dropdown_menu_unique($title, $controller, $place)
    {
        // Get CodeIgniter instance
        $CI =& get_instance();
        // Fetch the view variables
        $data = $CI->load->get_vars();

        $url = site_url($data['language'] . '/' . $controller . $place);

        return <<<HTML
        <a class="dropdown-item" href="{$url}">
            {$title}
        </a>
        HTML;
    }
}

if (!function_exists('menu_logo')) {
    function menu_logo($logo)
    {
        return <<<HTML
        <a type="button" class="nav-link text-decoration-none font-weight-bold" data-toggle="dropdown" href="#">
                <h4>{$logo} <i class="fas fa-caret-down"></i></h4>
            </a>
        HTML;
    }
}

if (!function_exists('nav_item')) {
    function nav_item($title, $controller, $place)
    {
        // Get CodeIgniter instance
        $CI =& get_instance();
        // Fetch the view variables
        $data = $CI->load->get_vars();

        $url = site_url($data['language'] . '/' . $controller . $place);

        return <<<HTML
            <li class="nav-item pb-2">
                <a class="nav-link text-decoration-none font-weight-bold" href="{$url}">
                    {$title}
                </a>
            </li>
            HTML;
    }
}