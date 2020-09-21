package hk.com.rubyicl.gpms.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UriUtils;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.OnClick;
import hk.com.rubyicl.gpms.MimeType;
import hk.com.rubyicl.gpms.R;
import hk.com.rubyicl.gpms.activity.NewMaterialActivity;
import hk.com.rubyicl.gpms.entity.RegulationEntity;
import hk.com.rubyicl.gpms.entity.RegulationItemEntity;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/3 下午 05:36
 *     description:
 *  <pre>
 */
public class ThreeFragment extends BaseFragment {
//    @BindView(R.id.data1_tv)
//    TextView data1_tv;
//    @BindView(R.id.data2_tv)
//    TextView data2_tv;

    @Override
    protected int getLayout() {
        return R.layout.fragmet_three;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private final int FILE_PICKER_REQUEST_CODE = 100;

    @OnClick({R.id.data1_tv, R.id.data2_tv, R.id.fragment_three_layout3})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.data1_tv:
                NewMaterialActivity.start(getContext(), 0);
                break;
            case R.id.data2_tv:
                ToastUtils.showShort(getString(R.string.data2));
                break;
            case R.id.fragment_three_layout3:
                PermissionX.init(this)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .onExplainRequestReason(new ExplainReasonCallback() {
                        @Override
                        public void onExplainReason(ExplainScope scope, List<String> deniedList) {
                            scope.showRequestReasonDialog(deniedList, getString(R.string.showRequestReason), "我已明白", "取消");
                        }
                    })
                    .onForwardToSettings(new ForwardToSettingsCallback() {
                        @Override
                        public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                            scope.showForwardToSettingsDialog(deniedList, getString(R.string.showForwardToSettings), "我已明白", "取消");
                        }
                    })
                    .request(new RequestCallback() {
                        @Override
                        public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                            if (allGranted) {
                                openFilePicker();
                            } else {
                                ToastUtils.showLong("您拒绝了读取文件的权限");
                            }
                        }
                    });
                break;
        }
    }

    /**
     * 打开安卓默认的文件选择器 并且设置只打开XLS 和 XLSX文件
     * 但是真机调试感觉过滤不太对
     */
    private void openFilePicker() {
        String[] mimeTypes = new String[]{MimeType.XLS, MimeType.XLSX};
        Intent filePickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        filePickerIntent.setType("application/pdf");
//        filePickerIntent.setType("video/*");
        filePickerIntent.setType("*/*");
        filePickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(filePickerIntent, FILE_PICKER_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                File file = UriUtils.uri2File(data.getData());
                LogUtils.d("选择的文件:" + file);
                ToastUtils.showLong("选择的文件:" + file);
                if (isExcelFile(file)) {
                    readExcel(file);
                } else {
                    ToastUtils.showLong("所选文件不是Excel文件");
                }
            } else {
                ToastUtils.showLong("无法读取选择文件的路径");
                LogUtils.e("无法读取选择文件的路径");
            }
        }
    }

    private void readExcel(File file) {
        int No_index = -1;
        int substances_name_cn_index = -1;
        int subtances_name_eg_index = -1;
        int CAS_No_index = -1;
        int threshold_index = -1;
        RegulationEntity regulationEntity = new RegulationEntity();
        try {
            InputStream stream = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(stream);
            int sheetCount = workbook.getNumberOfSheets();      //一个有多少个sheet
            XSSFSheet sheet = workbook.getSheetAt(1);   //获取第一张表
            int rowsCount = sheet.getPhysicalNumberOfRows();    //读取有多少行
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (int s = 1; s < sheetCount; s++) {
                for (int r = 0; r < rowsCount; r++) {   //竖向遍历 行遍历
                    Row row = sheet.getRow(r);
                    int cellsCount = row.getPhysicalNumberOfCells();    //获取该列有多少行
                    RegulationItemEntity regulationItemEntity = new RegulationItemEntity();
                    regulationItemEntity.setRegulationEntity(regulationEntity);
                    //每次读取一行的内容
                    for (int c = 0; c < cellsCount; c++) {
                        String cellContent = getCellAsString(row, c, formulaEvaluator);
                        //将每一格子的内容转换为字符串形式
                        //LogUtils.d("r = " + r + " c = " + c + " Cell = " + row.getCell(c).toString());
                        //如果第第1行1列那么就是 法规的名称
                        if (r == 0 && c == 0) {
                            regulationEntity.setName(cellContent);
                        }
                        //如果是第二行 那就是每一列的名字
                        if (r == 1) {
                            LogUtils.d("r = " + r + " c = " + c + " Cell = " + row.getCell(c).toString());
                            switch (cellContent) {
                                case "No":
                                    No_index = c;
                                    break;
                                case "物质名称":
                                    substances_name_cn_index = c;
                                    break;
                                case "Substances Name":
                                    subtances_name_eg_index = c;
                                    break;
                                case "CAS No.":
                                    CAS_No_index = c;
                                    break;
                                case "参考阈值/Threshold":
                                case "限制要求":
                                    threshold_index = c;
                                    break;
                            }
                            LogUtils.d(String.format("No_index = %d ,substances_name_cn_index = %d ,subtances_name_eg_index = %d ,CAS_No_index = %d, threshold_index = %d",
                                No_index, substances_name_cn_index, subtances_name_eg_index, CAS_No_index, threshold_index));
                        }
                        if (r > 1) {    //下标大于1的 都是法规正式内容了
                            if (c == No_index) {
                                regulationItemEntity.setNo(cellContent);
                            } else if (c == substances_name_cn_index) {
                                regulationItemEntity.setSubstances_name_cn(cellContent);
                            } else if (c == subtances_name_eg_index) {
                                regulationItemEntity.setSubstances_name_eg(cellContent);
                            } else if (c == CAS_No_index) {
                                regulationItemEntity.setCAS_No(cellContent);
                            } else if (c == threshold_index) {
                                regulationItemEntity.setThreshold(cellContent);
                            }
                            if (c == cellsCount - 1) {  //都读取完了
                                regulationItemEntity.save();    //保存Item到数据库
                                regulationEntity.getRegulationItemEntityList().add(regulationItemEntity);   //把当前Item添加到对应的表的list里面
                            }

                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        regulationEntity.save();
        ToastUtils.showLong("Excel 导入搞定!");
    }

    /**
     * 读取excel文件中每一行的内容
     *
     * @param row
     * @param c
     * @param formulaEvaluator
     * @return
     */
    private static String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    value = "" + Double.valueOf(numericValue).intValue();
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
                    break;
            }
        } catch (NullPointerException e) {
            /* proper error handling should be here */
            LogUtils.e("r: " + row.getRowNum() + " c: " + c + "为空");
        }
        return value;
    }

    private void parseTable(int sheet) {

    }

    private boolean isExcelFile(File file) {
        String extension = FileUtils.getFileExtension(file);
        return extension.equals("xlsx") || extension.equals("xls");
    }

}
