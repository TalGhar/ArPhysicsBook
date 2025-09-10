package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class DetailedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_activity)

        val videoView = findViewById<VideoView>(R.id.videoView)
        val infoText = findViewById<TextView>(R.id.infoText)
        val backButton = findViewById<ImageButton>(R.id.backButton)


        val imageName = intent.getStringExtra("IMAGE_NAME")

        when (imageName) {
            "oblozka.jpg" -> {
                playVideo(videoView, R.raw.titul)
                infoText.text = "Добро пожаловать в интерактивное учебное пособие по физике для 9 класса!\n" +
                        "Этот курс поможет вам лучше понять законы природы, познакомиться с ключевыми явлениями и закрепить материал с помощью наглядных опытов и экспериментов.\n" +
                        "\n" +
                        "В этом разделе вы найдёте:\n" +
                        "\n" +
                        "\uD83C\uDFA5 обучающие видео по темам курса;\n" +
                        "\uD83D\uDCDD пояснения и краткие конспекты;\n" +
                        "\uD83D\uDD2C примеры опытов и задач с реальными приложениями.\n" +
                        "Изучайте физику вместе с современными технологиями — так наука станет ближе и понятнее!"
            }
            "2ndNT.jpg" -> {
                playVideo(videoView, R.raw.seczn)
                infoText.text = "⚖ Второй закон Ньютона\n" +
                        "\n" +
                        "Основной текст:\n" +
                        "Второй закон Ньютона устанавливает связь между силой, действующей на тело, его массой и ускорением:\n" +
                        "\n" +
                        "\uD835\uDC39" +
                        "=" +
                        "\uD835\uDC5A" +
                        "⋅" +
                        "\uD835\uDC4E\n" +
                        "\n" +
                        "Где:\n" +
                        "\n" +
                        "F — сила, действующая на тело (в Ньютонах),\n" +
                        "\n" +
                        "m — масса тела (в килограммах),\n" +
                        "\n" +
                        "a — ускорение тела (в м/с²).\n" +
                        "\n" +
                        "Этот закон означает, что ускорение прямо пропорционально силе и обратно пропорционально массе.\n" +
                        "Чем больше сила — тем быстрее изменяется скорость. Чем больше масса — тем труднее разогнать тело.\n" +
                        "\n" +
                        "Примеры из жизни:\n" +
                        "\n" +
                        "\uD83D\uDE97 Автомобиль быстрее разгоняется при сильном нажатии на педаль газа.\n" +
                        "\n" +
                        "⚽ Лёгкий мяч легче ускорить ударом, чем тяжёлый.\n" +
                        "\n" +
                        "\uD83D\uDE80 Двигатели ракеты создают огромную силу, чтобы придать ускорение массивному кораблю."
            }
            "postdviz.jpg" -> {
                playVideo(videoView, R.raw.test)
                infoText.text = "поступательные движения"
            }
            "sila.jpg" -> {
                playVideo(videoView, R.raw.test)
                infoText.text = "Работа силы\n" +
                        "\n" +
                        "Работа показывает, сколько энергии передаётся телу:\n" +
                        "\n" +
                        "\uD835\uDC34" +
                        "=\n" +
                        "\uD835\uDC39" +
                        "⋅\n" +
                        "\uD835\uDC60" +
                        "⋅" +
                        "cos" +
                        "\u2061" +
                        "\uD835\uDEFC" +
                        "\n" +
                        "Она зависит от силы, пути и направления движения.\n" +
                        "\n" +
                        "Мощность\n" +
                        "\n" +
                        "Мощность — это работа, совершённая за время:\n" +
                        "\n" +
                        "\uD835\uDC43" +
                        "=" +
                        "\uD835\uDC34" +
                        "\uD835\uDC61" +
                        "или\n" +
                        "\uD835\uDC43" +
                        "=" +
                        "\uD835\uDC39" +
                        "⋅" +
                        "\uD835\uDC63" +
                        "\t\u200B\n" +
                        "\n" +
                        "\n" +
                        "Она показывает, как быстро совершается работа.\n" +
                        "\n" +
                        "Пример: двигатель автомобиля с большей мощностью быстрее разгоняет машину. \uD83D\uDE97⚡"
            }

            else -> {
                infoText.text = "Нет данных для этого маркера."
            }
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun playVideo(videoView: VideoView, videoRes: Int) {
        val uri = Uri.parse("android.resource://${packageName}/$videoRes")
        videoView.setVideoURI(uri)
        videoView.setOnPreparedListener { it.isLooping = true }
        videoView.start()
    }
}