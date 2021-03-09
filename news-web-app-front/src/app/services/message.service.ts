import { Injectable } from '@angular/core';
import { Stomp } from "@stomp/stompjs";
import * as Socket from 'socket.io-client';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor() {

  }

  webSocketEndPoint: string = 'ws://localhost:8060/TopicSubscriptionService/subscription-service/websocket';
  topic: string = "/topic/message/sub";
  stompClient: any;

  _connect() {
    console.log("Initialize WebSocket Connection");
    let ws = new Socket(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    this.stompClient.connect({}, function (frame) {
      this.stompClient.subscribe(this.topic, function (sdkEvent) {
        this.onMessageReceived(sdkEvent);
      });
      //_this.stompClient.reconnect_delay = 2000;
    }, this.errorCallBack);
  };

  _disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }

  // on error, schedule a reconnection attempt
  errorCallBack(error) {
    console.log("errorCallBack -> " + error)
    setTimeout(() => {
      this._connect();
    }, 5000);
  }

  /**
   * Send message to sever via web socket
   * @param {*} message 
   */
  _send(message) {
    console.log("calling logout api via web socket");
    this.stompClient.send("/app/hello", {}, JSON.stringify(message));
  }
}
