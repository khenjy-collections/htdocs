<?php
defined('BASEPATH') or exit('No direct script access allowed');

include 'Omnitags.php';

class C_tabel_b5 extends Omnitags
{
	// Halaman publik
	public function detail($param1 = null)
	{
		$this->declarew();
		$this->page_session_all();

		$tabel = $this->tl_b5->get_b5_by_b5_field1($param1)->result();
		$this->check_data($tabel);

		$data1 = array(
			'title' => lang('tabel_b5_alias_v8_title'),
			'konten' => $this->v8['tabel_b5'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_b5']),
			'tbl_b5' => $this->tl_b5->get_b5_by_b5_field1($param1),
		);

		$data = array_merge($data1, $this->package);

		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/template', $data);
	}

	// Halaman khusus akun


	// Halaman admin
	public function admin()
	{
		$this->declarew();
		$this->page_session_3();

		$param1 = $this->v_get['tabel_b5_field7'];

		$data1 = array(
			'title' => lang('tabel_b5_alias_v3_title'),
			'konten' => $this->v3['tabel_b5'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_b5']),
			'tbl_b5' => $this->tl_b5->get_all_b5(),
			'tbl_b7' => $this->tl_b7->get_all_b7(),
			'tabel_b5_field7_value' => $param1,
		);

		$data = array_merge($data1, $this->package);

		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/template', $data);
	}

	public function filter()
	{
		$this->declarew();
		$this->page_session_3();

		validate_input(
			array(
				$this->v_get['tabel_b5_field7'],
			),
			$this->views['flash1'],
			''
		);

		$param1 = $this->v_get['tabel_b5_field7'];

		$data1 = array(
			'title' => lang('tabel_b5_alias_v3_title'),
			'konten' => $this->v3['tabel_b5'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_b5']),
			'tbl_b5' => $this->tl_b5->get_b5_by_b5_field7($param1),
			'tbl_b7' => $this->tl_b7->get_all_b7(),
			'tabel_b5_field7_value' => $param1
		);

		$data = array_merge($data1, $this->package);

		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/template', $data);
	}

	public function tambah()
	{
		$this->declarew();
		$this->session_3();

		validate_input(
			array(
				$this->v_post['tabel_b5_field2'],
				$this->v_post['tabel_b5_field3'],
				$this->v_post['tabel_b5_field4'],
				$this->v_post['tabel_b5_field5'],
				$this->v_post['tabel_b5_field7'],
			),
			$this->views['flash2'],
			'tambah'
		);

		$tabel_b5_field2 = $this->v_post['tabel_b5_field2'];
		$method = $this->tl_c2->get_b5_by_b5_field2($tabel_b5_field2);

		// mencari apakah jumlah data kurang dari 1
		if ($method->num_rows() < 1) {

			$new_name = $this->v_post['tabel_b5_field2'];
			$path = $this->v_upload_path['tabel_b5'];

			$config['upload_path'] = $path;
			$config['allowed_types'] = $this->file_type1;
			$config['file_name'] = $new_name;
			$config['overwrite'] = TRUE;
			$config['remove_spaces'] = TRUE;

			$this->load->library('upload', $config);
			$upload = $this->upload->do_upload($this->v_input['tabel_b5_field4_input']);

			if (!$upload) {
				// Di sini seharusnya ada notifikasi modal kalau upload tidak berhasil
				// Tapi karena formnya sudah required saya rasa tidak perlu


				set_flashdata($this->views['flash2'], $this->flash_msg2['tabel_b5_field4_alias']);
				set_flashdata('modal', $this->views['flash2_func1']);
				redirect($_SERVER['HTTP_REFERER']);
			} else {
				// Di bawah ini adalah method untuk mengambil informasi dari hasil upload data
				$upload = $this->upload->data();
				$gambar = $upload['file_name'];
			}

			$data = array(
				$this->aliases['tabel_b5_field1'] => '',
				$this->aliases['tabel_b5_field2'] => $this->v_post['tabel_b5_field2'],
				$this->aliases['tabel_b5_field3'] => htmlspecialchars($this->v_post['tabel_b5_field3']),
				$this->aliases['tabel_b5_field4'] => $gambar,
				$this->aliases['tabel_b5_field5'] => $this->v_post['tabel_b5_field5'],
				$this->aliases['tabel_b5_field6'] => $this->aliases['tabel_b5_field6_value2'],
				$this->aliases['tabel_b5_field7'] => $this->v_post['tabel_b5_field7'],
			);

			$aksi = $this->tl_b5->insert_b5($data);

			$notif = $this->handle_4b($aksi, 'tabel_b5');

			redirect($_SERVER['HTTP_REFERER']);
		} else {

			set_flashdata($this->views['flash1'], $this->aliases['tabel_b5_field2'] . ' telah digunakan!');
			redirect($_SERVER['HTTP_REFERER']);
		}
	}

	public function update() //update tidak diperlukan di sini
	{
		$this->declarew();
		$this->session_3();

		$tabel_b5_field1 = $this->v_post['tabel_b5_field1'];

		$tabel = $this->tl_b5->get_b5_by_b5_field1($tabel_b5_field1)->result();
		$this->check_data($tabel);

		validate_input(
			array(
				$this->v_post['tabel_b5_field1'],
				$this->v_post['tabel_b5_field2'],
				$this->v_post['tabel_b5_field3'],
				$this->v_post['tabel_b5_field4_old'],
				$this->v_post['tabel_b5_field5'],
				$this->v_post['tabel_b5_field7'],
			),
			$this->views['flash3'],
			'ubah' . $tabel_b5_field1
		);


		$tabel_b5 = $this->tl_b5->get_b5_by_b5_field1($tabel_b5_field1)->result();
		$new_name = $this->v_post['tabel_b5_field2'];
		$path = $this->v_upload_path['tabel_b5'];
		$img = $this->v_post['tabel_b5_field4_old'];
		$extension = '.' . getExtension($path . $img);

		$config['upload_path'] = $path;
		$config['allowed_types'] = $this->file_type1;
		$config['file_name'] = $new_name;
		$config['overwrite'] = TRUE;
		$config['remove_spaces'] = TRUE;

		$this->load->library('upload', $config);
		$upload = $this->upload->do_upload($this->v_input['tabel_b5_field4_input']);

		if (!$upload) {
			if ($new_name != $tabel_b5[0]->nama) {
				rename($path . $img, $path . str_replace(' ', '_', $new_name) . $extension);
				$gambar = str_replace(' ', '_', $new_name) . $extension;
			} else {
				$gambar = $img;
			}
		} else {
			if ($new_name != $tabel_b5[0]->nama) {
				// File upload is successful, delete the old file
				if (file_exists($path . $img)) {
					unlink($path . $img);
				}
				$upload = $this->upload->data();
				$gambar = $upload['file_name'];
			} else {
				$gambar = $img;
			}
		}

		// menggunakan nama khusus sama dengan konfigurasi
		$data = array(
			$this->aliases['tabel_b5_field2'] => $this->v_post['tabel_b5_field2'],
			$this->aliases['tabel_b5_field3'] => htmlspecialchars($this->v_post['tabel_b5_field3']),
			$this->aliases['tabel_b5_field4'] => $gambar,
			$this->aliases['tabel_b5_field5'] => $this->v_post['tabel_b5_field5'],
			$this->aliases['tabel_b5_field7'] => $this->v_post['tabel_b5_field7'],
		);

		$aksi = $this->tl_b5->update_b5($data, $tabel_b5_field1);

		$notif = $this->handle_4c($aksi, 'tabel_b5', $tabel_b5_field1);

		redirect($_SERVER['HTTP_REFERER']);
	}

	public function sync_theme($tabel_b5_field7 = null)
	{
		$this->declarew();
		$this->session_3();

		$tabel = $this->tl_b7->get_b7_by_b7_field1($tabel_b5_field7)->result();
		$this->check_data($tabel);

		$data = array(
			$this->aliases['tabel_b5_field7'] => $tabel_b5_field7,
		);

		$aksi = $this->tl_b5->update_all_b5($data);

		$notif = $this->handle_4c($aksi, 'tabel_b5', $tabel_b5_field7);

		redirect($_SERVER['HTTP_REFERER']);
	}

	public function aktifkan($tabel_b5_field1 = null) //update tidak diperlukan di sini
	{
		$this->declarew();
		$this->session_3();

		$tabel = $this->tl_b5->get_b5_by_b5_field1($tabel_b5_field1)->result();
		$this->check_data($tabel);
		// menggunakan nama khusus sama dengan konfigurasi
		$data = array(
			$this->aliases['tabel_b5_field6'] => $this->aliases['tabel_b5_field6_value1'],
		);

		$aksi = $this->tl_b5->update_b5($data, $tabel_b5_field1);

		$notif = $this->handle_4c($aksi, 'tabel_b5_field6', $tabel_b5_field1);

		redirect($_SERVER['HTTP_REFERER']);
	}

	public function nonaktifkan($tabel_b5_field1 = null) //update tidak diperlukan di sini
	{
		$this->declarew();
		$this->session_3();

		$tabel = $this->tl_b5->get_b5_by_b5_field1($tabel_b5_field1)->result();
		$this->check_data($tabel);

		// menggunakan nama khusus sama dengan konfigurasi
		$data = array(
			$this->aliases['tabel_b5_field6'] => $this->aliases['tabel_b5_field6_value2'],
		);

		$aksi = $this->tl_b5->update_b5($data, $tabel_b5_field1);

		$notif = $this->handle_4c($aksi, 'tabel_b5_field6', $tabel_b5_field1);

		redirect($_SERVER['HTTP_REFERER']);
	}

	public function delete($tabel_b5_field1 = null)
	{
		$this->declarew();
		$this->session_3();

		$tabel_b5 = $this->tl_b5->get_b5_field1($tabel_b5_field1)->result();
		$this->check_data($tabel_b5);
		$img = $tabel_b5[0]->img;

		unlink($this->v_upload_path['tabel_b5'] . $img);

		$aksi = $this->tl_b5->delete_b5($tabel_b5_field1);

		$notif = $this->handle_4e($aksi, 'tabel_b5', $tabel_b5_field1);

		redirect($_SERVER['HTTP_REFERER']);
	}

	// Cetak semua data
	public function laporan()
	{
		$this->declarew();
		$this->page_session_3();

		$data1 = array(
			'title' => lang('tabel_b5_alias_v4_title'),
			'konten' => $this->v4['tabel_b5'],
			'dekor' => $this->tl_b1->dekor($this->theme_id, $this->aliases['tabel_b5']),
			'tbl_b5' => $this->tl_b5->get_all_b5(),
		);

		$data = array_merge($data1, $this->package);

		set_userdata('previous_url', current_url(), 6000);
		load_view_data('_layouts/printpage', $data);
	}

	// Cetak satu data
}
