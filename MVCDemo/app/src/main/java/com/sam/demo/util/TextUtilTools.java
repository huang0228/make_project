package com.sam.demo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * 文字高亮显示
 * @author sam.huang
 *date:2018.11.29
 */

public class TextUtilTools {
	/**
	 * 关键字高亮显示
	 * 
	 * @param target  需要高亮的关键字
	 * @param text	     需要显示的文字
	 * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
	 */
	public static SpannableStringBuilder highlight(String text, String target,int color) {
		SpannableStringBuilder spannable = new SpannableStringBuilder(text);
		if (!target.equals(""))
		{
			CharacterStyle span = null;
			Pattern p = Pattern.compile(target);
			Matcher m = p.matcher(text);
			while (m.find()) {
				span = new ForegroundColorSpan(color);// 需要重复！
				spannable.setSpan(span, m.start(), m.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannable;
	}
	/**
	 * 关键字高亮显示加特殊字符
	 *
	 * @param target  需要高亮的关键字
	 * @param text	     需要显示的文字
	 * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
	 */
	public static SpannableStringBuilder highlightJia(String text, String target,int color) {
		SpannableStringBuilder spannable = new SpannableStringBuilder(text);
		if (!target.equals(""))
		{
			CharacterStyle span = null;
			Pattern p = Pattern.compile("(\\+)"+target);
			Matcher m = p.matcher(text);
			while (m.find()) {
				span = new ForegroundColorSpan(color);// 需要重复！
				spannable.setSpan(span, m.start(), m.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannable;
	}


	/**
	 * 关键字高亮显示加特殊字符
	 *
	 * @param target  需要高亮的关键字
	 * @param text	     需要显示的文字
	 * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
	 */
	public static SpannableStringBuilder highlightKuoHao(String text, String target,int color) {
		SpannableStringBuilder spannable = new SpannableStringBuilder(text);
		if (!target.equals(""))
		{
			CharacterStyle span = null;
			Pattern p = Pattern.compile("(\\()"+target+"(\\))");
			Matcher m = p.matcher(text);
			while (m.find()) {
				span = new ForegroundColorSpan(color);// 需要重复！
				spannable.setSpan(span, m.start(), m.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannable;
	}




	/**
	 * 设置textView结尾...后面显示的文字和颜色
	 * @param context 上下文
	 * @param textView textview
	 * @param minLines 最少的行数
	 * @param originText 原文本
	 * @param endText 结尾文字
	 * @param endColorID 结尾文字颜色id
	 * @param isExpand 当前是否是展开状态
	 */
	public static void toggleEllipsize(final Context context,
									   final TextView textView,
									   final int minLines,
									   final String originText,
									   final TextView endText,
									   final int endColorID,
									   final boolean isExpand) {
		if (TextUtils.isEmpty(originText)) {
			endText.setVisibility(View.GONE);
			return;
		}
		textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
				.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (isExpand) {
					textView.setText(originText);
					//endText.setVisibility(View.GONE);
				} else {
					int paddingLeft = textView.getPaddingLeft();
					int paddingRight = textView.getPaddingRight();
					TextPaint paint = textView.getPaint();
					float moreText = textView.getTextSize() * endText.getWidth();
					float availableTextWidth = (textView.getWidth() - paddingLeft - paddingRight) *
							minLines - moreText;
					CharSequence ellipsizeStr = TextUtils.ellipsize(originText, paint,
							availableTextWidth, TextUtils.TruncateAt.END);
					if (ellipsizeStr.length() < originText.length()) {
						String temp = ellipsizeStr.toString()+endText.getText().toString();
						SpannableStringBuilder sbStr=highlight(temp,endText.getText().toString(), context.getResources().getColor(endColorID));
						textView.setText(sbStr);
						//endText.setVisibility(View.VISIBLE);
					} else {
						//endText.setVisibility(View.GONE);
						textView.setText(originText);
					}
				}
				if (Build.VERSION.SDK_INT >= 16) {
					textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				} else {
					textView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			}
		});
	}
}
