<?php
defined('BASEPATH') or exit('No direct script access allowed');

class M_tabel_e1 extends CI_Model
{
	public function __construct() {
        parent::__construct();
        // Load SSH library or helper here
    }

    public function fetchDataViaSSH() {
        // Code to connect to SSH server and fetch data
    }
	
	public function get_all_e1()
	{
		$this->db->order_by($this->aliases['tabel_e1_field1'], 'DESC');
		return $this->db->get($this->aliases['tabel_e1']);
	}

	public function filter($param1)
	{
		$this->db->where($this->aliases['tabel_e1_field2'], $param1);
		$this->db->order_by($this->aliases['tabel_e1_field1'], 'DESC');
		return $this->db->get($this->aliases['tabel_e1']);
	}

	public function get_e1_by_e1_field1($param1)
	{
		$this->db->where($this->aliases['tabel_e1_field1'], $param1);
		$this->db->order_by($this->aliases['tabel_e1_field1'], 'DESC');
		return $this->db->get($this->aliases['tabel_e1']);
	}

	public function insert_e1($data)
	// public function insert_e1($query)
	{
		// include "application/config/database.php";
		// return mysqli_query($db(''), $query);
		return $this->db->insert($this->aliases['tabel_e1'], $data);
	}

	public function update_e1($data, $param1)
	{
		$this->db->where($this->aliases['tabel_e1_field1'], $param1);
		return $this->db->update($this->aliases['tabel_e1'], $data);
	}

	public function delete_e1($param1)
	{
		$this->db->where($this->aliases['tabel_e1_field1'], $param1);
		return $this->db->delete($this->aliases['tabel_e1']);
	}
}
