/**
 * @Title MaxMinEditWatcher.java
 * @Package com.mingseal.listener
 * @Description TODO
 * @author 商炎炳
 * @date 2016年1月27日 上午10:37:45
 * @version V1.0
 */
package com.mingseal.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @ClassName MaxMinEditWatcher
 * @Description 大于最大值时,取最大值;小于最小值时,取最小值 (针对小数位)
 * @author 商炎炳
 * @date 2016年1月27日 上午10:37:45
 *
 */
public class MaxMinEditDoubleWatcher implements TextWatcher {

	/**
	 * @Fields maxValue: 最大值
	 */
	private Double maxValue;
	/**
	 * @Fields minValue: 最小值
	 */
	private Double minValue;
	/**
	 * @Fields etNumber: 当前Edittext控件
	 */
	private EditText etNumber;
	/**
	 * @Fields num: 获取当前Edittext中的值
	 */
	private Double num;
	/**
	 * @Fields markVal: 初始值=minValue
	 */
	private Double markVal;

	/**
	 * @Title MaxMinEditWatcher
	 * @Description if(>=maxValue)取maxValue;else(<=minValue)取minValue
	 * @param maxValue
	 *            允许输入的最大值
	 * @param minValue
	 *            允许输入的最小值
	 * @param etNumber
	 *            当前Edittext控件
	 */
	public MaxMinEditDoubleWatcher(Double maxValue, Double minValue, EditText etNumber) {
		super();
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.etNumber = etNumber;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (start > 1) {
			try {
				num = Double.parseDouble(s.toString());
			}catch (NumberFormatException e){
				num=minValue;
			}
			if (num > maxValue) {
				s = String.valueOf(maxValue);
				etNumber.setText(s);
			} else if (num < minValue) {
				s = String.valueOf(minValue);
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s != null && !s.equals("")) {
			markVal = minValue;
			try {
				markVal = Double.parseDouble(s.toString());
			} catch (NumberFormatException e) {
				markVal = minValue;
			}
			if (markVal > maxValue) {
				etNumber.setText(String.valueOf(maxValue));
			}
		}
	}

}