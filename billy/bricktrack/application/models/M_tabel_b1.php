<?php
defined('BASEPATH') or exit('No direct script access allowed');

class M_tabel_b1 extends CI_Model
{
	public function get_all_b1()
	{
		$this->db->order_by($this->aliases['tabel_b1_field1'], 'DESC');
		return $this->db->get($this->aliases['tabel_b1']);
	}

	public function dekor($param1, $param2)
	{
		$this->db->where($this->aliases['tabel_b1_field7'], $param1);
		$this->db->where($this->aliases['tabel_b1_field2'], $param2);
		$this->db->order_by($this->aliases['tabel_b1_field1'], 'DESC');
		return $this->db->get($this->aliases['tabel_b1']);
	}

	public function filter($param1)
	{
		$this->db->where($this->aliases['tabel_b1_field7'], $param1);
		$this->db->order_by($this->aliases['tabel_b1_field1'], 'DESC');
		return $this->db->get($this->aliases['tabel_b1']);
	}

	public function get_b1_by_b1_field1($param1)
	{
		$this->db->where($this->aliases['tabel_b1_field1'], $param1);
		$this->db->order_by($this->aliases['tabel_b1_field1'], 'DESC');
		return $this->db->get($this->aliases['tabel_b1']);
	}

	public function get_b1_by_b1_field2($param1)
	{
		$this->db->where($this->aliases['tabel_b1_field2'], $param1);
		$this->db->order_by($this->aliases['tabel_b1_field1'], 'DESC');
		return $this->db->get($this->aliases['tabel_b1']);
	}

	public function get_b1_by_b1_field2_by_b1_field7($param1, $param2)
	{
		$this->db->where($this->aliases['tabel_b1_field2'], $param1);
		$this->db->where($this->aliases['tabel_b1_field7'], $param2);
		$this->db->order_by($this->aliases['tabel_b1_field1'], 'DESC');
		return $this->db->get($this->aliases['tabel_b1']);
	}

	public function insert_b1($data)
	{
		return $this->db->insert($this->aliases['tabel_b1'], $data);
	}

	public function update_b1($data, $param1)
	{
		$this->db->where($this->aliases['tabel_b1_field1'], $param1);
		return $this->db->update($this->aliases['tabel_b1'], $data);
	}

	public function update_all_b1($data)
	{
		return $this->db->update($this->aliases['tabel_b1'], $data);
	}

	public function delete_b1($param1)
	{
		$this->db->where($this->aliases['tabel_b1_field1'], $param1);
		return $this->db->delete($this->aliases['tabel_b1']);
	}

	public function delete_b1_by_b1_field7($param1)
	{
		$this->db->where($this->aliases['tabel_b1_field7'], $param1);
		return $this->db->delete($this->aliases['tabel_b1']);
	}
}
