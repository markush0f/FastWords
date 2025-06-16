package com.fastwords.fastwords.services;

public interface GameConnectionService {

    void addConnectedPlayer(Long gameId, Long userId);

    void notifyGameStart(Long gameId);
}
