package ru.cfuv.cfuvscheduling

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TimetableScreen(date: Date) {

    Column(Modifier.fillMaxSize()) {
        //        Типо верхнее меню
        Column(
            Modifier
                .height(60.dp)
                .fillMaxSize()
                .background(Color.Green)
        ) {

        }
//        /////////////////

        val dateInString = SimpleDateFormat("dd.MM.yyyy").format(date)

        val weekDay = SimpleDateFormat("EEEE", Locale("ru")).format(date)

        Column(Modifier.padding(10.dp)) {
            Text(
                text = dateInString,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = weekDay.capitalize(),
                fontSize = 18.sp
            )
        }

        Column(
            Modifier
                .weight(1f)
        ) {

            val classObj1 = DummyClassBom()
            classObj1.classCode = "СТП"
            classObj1.className = ""
            classObj1.classType = "Семинар"
            classObj1.teacherContactName = "Чабанов Владимир Викторович"
            classObj1.room = "8А"
            classObj1.classNumber = "1"
            classObj1.startTime = "8:00"
            classObj1.finishTime = "9:30"

            val classObj2 = DummyClassBom()
            classObj2.classCode = "ВМ"
            classObj2.className = ""
            classObj2.classType = "Лекция"
            classObj2.teacherContactName = "Смирнова Светлана Ивановна"
            classObj2.room = "211А"
            classObj2.classNumber = "2"
            classObj2.startTime = "9:50"
            classObj2.finishTime = "11:20"

            val classObj3 = DummyClassBom()
            classObj3.classCode = "ОС"
            classObj3.className = ""
            classObj3.classType = "Семинар"
            classObj3.teacherContactName = "Зойкин Евгений Сергеевич"
            classObj3.room = "120А"
            classObj3.classNumber = "3"
            classObj3.startTime = "11:30"
            classObj3.finishTime = "13:00"


            Spacer(modifier = Modifier.height(10.dp))
            ClassItem(classObj1)

            Spacer(modifier = Modifier.height(10.dp))
            ClassItem(classObj2)

            Spacer(modifier = Modifier.height(10.dp))
            ClassItem(classObj3)

        }

        //        Типо нижнее меню
        Column(
            Modifier
                .height(100.dp)
                .fillMaxSize()
                .background(Color.Cyan)
        ) {
        }
//        ///////////////////////////////////
    }
}

@Composable
fun ClassNumberItem(
    classNumber: String,
    startTime: String,
    finishTime: String,
    modifier: Modifier,
    classNumberFontSize: TextUnit,
    classPeriodFontSize: TextUnit
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = classNumber,
            fontSize = classNumberFontSize,
            textAlign = TextAlign.Center
        )
        Text(
            text = "$startTime - $finishTime",
            fontSize = classPeriodFontSize,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ClassInfoItem(classObj: DummyClassBom, modifier: Modifier) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center
    ) {
        val textColor = Color.White
        Row {
            Text(
                text = classObj.classCode,
                fontSize = 14.sp,
                lineHeight = 15.sp,
                modifier = Modifier.width(40.dp),
                color = textColor
            )

            Text(
                text = "(${classObj.classType})",
                fontSize = 14.sp,
                lineHeight = 15.sp,
                color = textColor
            )
        }

        Text(
            text = classObj.teacherContactName,
            fontSize = 14.sp,
            lineHeight = 15.sp,
            color = textColor
        )

        Text(
            text = classObj.room,
            fontSize = 14.sp,
            lineHeight = 15.sp,
            color = textColor
        )

    }

}

@Composable
fun ClassItem(classObj: DummyClassBom) {

    val itemHeight = 80.dp

    Row(Modifier.padding(horizontal = 5.dp)) {
        ClassNumberItem(
            classObj.classNumber,
            classObj.startTime,
            classObj.finishTime,
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .height(itemHeight)
                .padding(vertical = 10.dp),
            classNumberFontSize = 16.sp,
            classPeriodFontSize = 14.sp
        )


        Spacer(modifier = Modifier.padding(5.dp))

        // TODO: aleksio: подобрать цвета для каждого типа занятий и выразить их в виде констант
        val color = when (classObj.classType) {
            "Семинар" -> Color.Blue
            "Лекция" -> Color(255, 155, 0)
            else -> Color.Transparent
        }

        ClassInfoItem(
            classObj,
            Modifier
                .clip(RoundedCornerShape(15))
                .background(color)
                .weight(3.5f)
                .height(itemHeight)
                .padding(vertical = 10.dp)
                .padding(start = 25.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun TimetableScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        TimetableScreen(Date(2024 - 1900, 2, 30))
    }

}