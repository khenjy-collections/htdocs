<table class="table">
  <thead class="thead">
    <tr>
      <th><?= lang('tabel_e4_field1_alias') ?></th>
      <th><?= lang('tabel_e4_field2_alias') ?></th>
      <th><?= lang('tabel_e4_field3_alias') ?></th>
      <th><?= lang('tabel_e4_field4_alias') ?></th>
      <th><?= lang('tabel_e4_field5_alias') ?></th>
    </tr>
  </thead>
  <tbody>
    <?php foreach ($tbl_e4->result() as $tl_e4): ?>
      <tr>
        <td width=""><?= $tl_e4->$tabel_e4_field1 ?></td>
        <td width=""><?= $tl_e4->$tabel_e4_field2 ?></td>
        <td width=""><?= $tl_e4->$tabel_e4_field3 ?></td>
        <td width=""><?= $tl_e4->$tabel_e4_field4 ?></td>
        <td width=""><?= $tl_e4->$tabel_e4_field5 ?></td>
      </tr>
    <?php endforeach ?>
  </tbody>
</table>