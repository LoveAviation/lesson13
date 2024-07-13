package com.example.lesson12

sealed class State {
    object loading : State()
    object stop : State()
    object succes : State()
}