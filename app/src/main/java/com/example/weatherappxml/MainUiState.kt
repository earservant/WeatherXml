package com.example.weatherappxml

import com.example.weatherappxml.adapters.WeatherModel

/**
 * Список состояний главного экрана бла-бла-бла
 */
sealed interface MainUiState {
    /**
     * Состояние загрузки, до тех пора пока не получим данные находимся в этом состоянии. Является стартовым состоянием экрана
     */
    object Loading : MainUiState

    /**
     * Состояние ошибки при получении данных
     *
     * @param errorMessage текст сообщения об ошибке
     */
    class Error(val errorMessage: String) : MainUiState

    /**
     * Состояние при успешном получении данных
     *
     * @param data данные
     */
    class Data(val data: WeatherModel) : MainUiState
}