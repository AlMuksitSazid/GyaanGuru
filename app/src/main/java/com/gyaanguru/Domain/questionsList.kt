package com.gyaanguru.Domain

//List of questions for example. Questions also get from API service.
 fun questionsList(): MutableList<QuestionModel> {
    val question: MutableList<QuestionModel> = mutableListOf()
    question.add(
        QuestionModel(
            1,
            "Which planet is the largest in the solar system?",
            "Earth",
            "Mars",
            "Neptune",
            "Jupiter",
            "d",
            5,
            "q_1",
            null
        )
    )
    question.add(
        QuestionModel(
            2,
            "Which country is the largest in the world by land area?",
            "Russia",
            "Canada",
            "United States",
            "China",
            "a",
            5,
            "q_2",
            null
        )
    )
    question.add(
        QuestionModel(
            3,
            "Which of the following substances is used as anti-cancer medication in the medical world?",
            "Cheese",
            "Lemon Juice",
            "Cannabis",
            "Paspalum",
            "c",
            5,
            "q_3",
            null
        )
    )
    question.add(
        QuestionModel(
            4,
            "What is the capital of France?",
            "Paris",
            "London",
            "Berlin",
            "Madrid",
            "a",
            5,
            "q_4",
            null
        )
    )
    question.add(
        QuestionModel(
            5,
            "Which of the following symbols represents the chemical element with the atomic number 6?",
            "O",
            "H",
            "C",
            "N",
            "c",
            5,
            "q_5",
            null
        )
    )
    question.add(
        QuestionModel(
            6,
            "What is the largest mammal?",
            "Elephant",
            "Giraffe",
            "Blue Whale",
            "Hippopotamus",
            "c",
            5,
            "q_6",
            null
        )
    )
    question.add(
        QuestionModel(
            7,
            "What is the largest ocean in the world?",
            "Pacific Ocean",
            "Atlantic Ocean",
            "Indian Ocean",
            "Arctic Ocean",
            "a",
            5,
            "q_7",
            null
        )
    )
    question.add(
        QuestionModel(
            8,
            "What is the chemical symbol for gold?",
            "Au",
            "Ag",
            "Fe",
            "Cu",
            "a",
            5,
            "q_8",
            null
        )
    )
    question.add(
        QuestionModel(9,
            "In which continent are the most independent countries located?",
            "Asia",
            "Europe",
            "Africa",
            "Americas",
            "c",
            5,
            "q_9",
            null
        )
    )
    question.add(
        QuestionModel(10,
            "What is the capital of Spain?",
            "Madrid",
            "Barcelona",
            "Valencia",
            "Seville",
            "a",
            5,
            "q_10",
            null
        )
    )
    return question
}
