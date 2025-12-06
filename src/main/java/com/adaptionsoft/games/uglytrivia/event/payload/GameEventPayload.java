package com.adaptionsoft.games.uglytrivia.event.payload;

public sealed interface GameEventPayload permits CorrectAnswerPayload, PenaltyBoxReleaseStatusPayload, PlayerAddedPayload, PlayerRolledPayload, RollOutcomePayload, WrongAnswerPayload {
}
