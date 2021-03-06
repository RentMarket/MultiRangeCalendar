package lecalendar.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View

import lecalendar.model.DayModel
import java.util.Calendar
import java.util.Date

/**
 * Created by chanlevel on 15/9/7.
 */


public class DayView : View {

    var daymodel: DayModel = DayModel(false, false, null, 0, 0, null, Date(), false, false, false, false, false);
    var mPaint = Paint();
    val upColor = Color.argb(0xff, 0xf3, 0x70, 0x70);
    val midColor = Color.argb(0xff, 0x66, 0x66, 0x66);
    val belowColor = Color.argb(0xff, 0xed, 0xaa, 0x70);
    var backgroudColor = Color.argb(0xff, 0xfc, 0xfc, 0xfc);;
    val dividerColor = Color.argb(0xff, 0xd5, 0xd5, 0xd5);
    val festivalBackColor = Color.argb(0xff, 0xff, 0xc6, 0x52);

    val OPENBACKCOLOR = Color.argb(0xff, 0xfc, 0xfc, 0xfc);
    val CLOSEBACKCOLOR = Color.argb(0xff, 0xe8, 0xe8, 0xe8);
    val CLOSETEXTCLOR = Color.argb(0xff, 0x99, 0x99, 0x99);
    var destiny = DisplayMetrics().density.toInt();
    var open = true;
    var selecting = false

    var SELELCTEDCOLOR = Color.argb(0xaa, 0xf3, 0x70, 0x70);
    var UNABLETEXTCOLOR=Color.argb(0xff,0xbd,0xc5,0xca)
    var mSelectType: SElECTTYPE = SElECTTYPE.STATUS;

    public constructor(context: Context) : super(context) {
        init()
    }

    public constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    public constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }


    private fun init() {
        destiny = getContext().getResources().getDisplayMetrics().density.toInt()

        setOnClickListener {
            v ->
        }

    }


    public fun setDayModel(daymodel: DayModel) {
        this.daymodel = daymodel;
        open = daymodel.room_num > 0
        selecting = daymodel.selecting
        // Log.d("DAYVIEW", "setdaymodel");
        invalidate()
    }

    public fun setStatus(open: Boolean) {
        this.open = open;
        // Log.d("DAYVIEW", "setStatus:" + open);
        invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

    }

    override fun onDraw(canvas: Canvas) {
        // Log.d("DAYVIEW","onDraw");
        super<View>.onDraw(canvas)

        if (daymodel.beforeToday) backgroudColor = OPENBACKCOLOR
        else
            if (selecting) backgroudColor = SELELCTEDCOLOR
            else
                if (!open) backgroudColor = CLOSEBACKCOLOR;
                else backgroudColor = OPENBACKCOLOR
        //mPaint.setColor(backgroudColor)
      //  canvas.drawRect(0f, 0f, getMeasuredWidth().toFloat(), getMeasuredHeight().toFloat(), mPaint)
          setBackgroundColor(backgroudColor)
      
        var c: Calendar = Calendar.getInstance();
        c.setTime(daymodel.date);

        var totalHeight = getMeasuredHeight() - getPaddingBottom() - getPaddingTop();
        var totalWidh = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

        //draw festivalback
        //        var testPaint :Paint=Paint();
        //        testPaint.setAntiAlias(true)
        //        testPaint.setColor(Color.BLUE)
        //        testPaint.setStrokeWidth(15f)
        //        canvas.drawPoint(getMeasuredWidth()/2f,getMeasuredHeight()/2f,testPaint);

        if (daymodel.isFestival&&!daymodel.beforeToday) {
            var fbradius = totalHeight * 1f / 4f;
            mPaint.setAntiAlias(true)
            mPaint.setColor(festivalBackColor);
            canvas.drawCircle(getMeasuredWidth() / 2f, getMeasuredHeight() / 2f, fbradius, mPaint);
        }

        //draw festival
        //   var festivalHeight = totalHeight * 1f / 4f;
        var festTextHeight = totalHeight * 1f / 5f;
        var textPaint = Paint();
        textPaint.setAntiAlias(true)
        textPaint.setTextSize(festTextHeight);
        textPaint.setColor(upColor);
        textPaint.setTextAlign(Paint.Align.CENTER)
        var up: String ? = "";
     //   if (daymodel.isFestival && !TextUtils.isEmpty(daymodel.festival)) up = daymodel.festival;
       // else
        if (daymodel.isFirstDayofMonth) {
            up = (1 + c.get(Calendar.MONTH)).toString() + "月"
        } else if (daymodel.isToday) up = "今天"
        var testWidth = textPaint.measureText(up);

        var festPosY = (getPaddingTop() + festTextHeight - textPaint.descent() / 2)
        //  Log.d("super-posY","as:"+textPaint.ascent().toString()+"-ds;"+textPaint.descent().toString())
        if(!daymodel.beforeToday)
        canvas.drawText(up, getPaddingLeft().toFloat() + testWidth.toFloat() / 2f, festPosY, textPaint);


        //draw mid
        //  var midY = getPaddingTop() + festivalHeight + 5;
        var midTextHeight = totalHeight * 1f / 3f;

        textPaint.setColor(if(daymodel.beforeToday) UNABLETEXTCOLOR  else if (open) midColor else CLOSETEXTCLOR);
        textPaint.setTextSize(midTextHeight);

        var mid = c.get(Calendar.DATE)
        var midTextPosY = getPaddingTop() / 2 + totalHeight / 2f + midTextHeight / 2f ;
        canvas.drawText(mid.toString(), totalWidh.toFloat() / 2f + getPaddingLeft(), midTextPosY, textPaint);

        //draw price
        var belowY = totalHeight + getPaddingTop() - textPaint.descent() / 2f ;
        var priceTextHeight = totalHeight / 4f;

        textPaint.setColor( if(selecting) Color.WHITE else if (open) belowColor else CLOSETEXTCLOR);

        textPaint.setTextSize(priceTextHeight);
        var price = daymodel.price ;
        var room_num = daymodel.room_num;
        // var belowText= if (room_num == 0) "无房" else room_num.toString() + "间";
        var belowText: String ;
        if (mSelectType == SElECTTYPE.STATUS) {
            belowText = if (room_num == 0) "无房" else room_num.toString() + "间";
        } else {
            belowText = price.toString();
        }
        //var belowText = impl?.onDay(daymodel.date, daymodel.price, daymodel.room_num);
        if (!daymodel.beforeToday)
            canvas.drawText(belowText, totalWidh.toFloat() / 2f + getPaddingLeft(), belowY.toFloat(), textPaint);

        mPaint.setColor(dividerColor);

        mPaint.setStrokeWidth(1f * destiny)
        if (daymodel.isFirstWeekinMonth) {
            canvas.drawLine(0f, 0f, getMeasuredWidthAndState().toFloat(), 0f, mPaint)
        };
        if (daymodel.isFirstDayofMonth) {
            canvas.drawLine(0f, 0f, 0f, getMeasuredHeightAndState().toFloat().toFloat(), mPaint)
        };
        //        if (daymodel.isLastWeekinMonth) {
        //            canvas.drawLine(0f, getMeasuredHeight().toFloat(), getMeasuredWidth().toFloat(), getMeasuredHeight().toFloat(), mPaint)
        //        };


    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {

        var w = getMeasuredWidth();
        //  var h=getMeasuredHeight();

        var newheight = View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.EXACTLY);
        super<View>.onMeasure(widthSpec, newheight)

    }

    public fun toggle() {
        setStatus(!open)


    }

    fun setSelectingStatus(selecting: Boolean) {
        this.selecting = selecting;
        invalidate()
    }

    fun setSelectType(type: SElECTTYPE) {
        this.mSelectType = type;
        invalidate();
    }

    enum class SElECTTYPE {
        STATUS, PRICE
    }


    var impl: NoticeDisplayImpl? = null;

    public fun setNotifceDisplayImpl(impl: NoticeDisplayImpl) {
        this.impl = impl;
    }

    public interface NoticeDisplayImpl {
        fun onDay(date: Date, price: Int, room_num: Int): String;
    }


    var callBack: DayViewCallBack ? = null;

    public fun setdayViewCallBack(callback: DayViewCallBack) {
        this.callBack = callback;
    }

    public interface DayViewCallBack {
        fun callBack(v: View, dayModel: DayModel?)
    }

    override fun setBackgroundColor(color: Int) {
        // Log.d("DAYVIEW","setBackgroundColor");
        super<View>.setBackgroundColor(color);
    }


}