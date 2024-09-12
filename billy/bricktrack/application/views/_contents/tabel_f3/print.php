<!-- menampilkan data pesanan sebagai ps -->
<?php foreach ($tbl_f3->result() as $tl_f3): ?>
  <!-- menampilkan data pemesan -->
  <table class="table">
    <thead class="thead">
      <tr>
        <th><?= lang('tabel_f3_field1_alias') ?></th>
        <th><?= lang('tabel_f3_field2_alias') ?></th>
        <th><?= lang('tabel_f3_field3_alias') ?></th>
        <th><?= lang('tabel_f3_field4_alias') ?></th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td width=""><?= $tl_f3->$tabel_f3_field1 ?></td>
        <td width=""><?= $tl_f3->$tabel_f3_field2 ?></td>
        <td width=""><?= $tl_f3->$tabel_f3_field3 ?></a>
        <td width=""><?= $tl_f3->$tabel_f3_field4 ?></td>
      </td>
    </tr>
  </tbody>
</table>

<!-- menampilkan data tamu -->
<table class="table">
    <thead class="thead">
      <tr>
        <th><?= lang('tabel_f3_field5_alias') ?></th>
        <th><?= lang('tabel_f3_field6_alias') ?></th>
        <th><?= lang('tabel_f3_field7_alias') ?></th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td width=""><?= $tl_f3->$tabel_f3_field5 ?></td>
        <td width=""><?= $tl_f3->$tabel_f3_field6 ?></td>
        <td width=""><?= $tl_f3->$tabel_f3_field7 ?></td>
        </td>
      </tr>
    </tbody>
  </table>

  <?php
endforeach ?>
<p class="text-center">Kirimkan bukti ini ke <?= $tabel_c2_field6_value4_alias ?> untuk diproses</p>