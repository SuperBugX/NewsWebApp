import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';

@Injectable({
  providedIn: 'root',
})
export class WebsocketService {
  //Attributes
  websocket: WebSocket;
  isConnected: boolean;
  currentUrl: string;

  //STOMP Related Attributes
  stompClient: Stomp.Client;
  subscriptions: string[];

  constructor() {
    this.currentUrl = '';
    this.isConnected = false;
    this.websocket = null;
    this.stompClient = null;
    this.subscriptions = [];
  }

  ngOnDestroy() {
    this.disconnect();
  }

  async connect(url: string) {
    return new Promise((resolve, reject) => {
      if (!this.isConnected) {
        this.websocket = new WebSocket(url);
        if (this.websocket) {
          this.stompClient = Stomp.over(this.websocket);
          if (this.stompClient) {
            this.stompClient.connect(
              {},
              (frame) => {
                this.isConnected = true;
                resolve(true);
              },
              (error) => {
                this.isConnected = false;
                reject(false);
              }
            );
          } else {
            this.isConnected = false;
            reject(false);
          }
        }
      } else {
        resolve(true);
      }
    });
  }

  disconnect(): void {
    if (this.isConnected) {
      this.websocket.close();
      this.websocket = null;
      this.stompClient = null;
      this.isConnected = false;
    }
  }

  sendMessage(destination: string, message: string): void {
    if (this.isConnected) {
      this.stompClient.send(destination, {}, message);
    } else {
      console.log('No Active Connection');
    }
  }

  subscribe(destination, callBack: (message) => void): void {
    if (this.isConnected) {
      if (this.subscriptions.indexOf(destination) == -1) {
        this.stompClient.subscribe(destination, callBack);
        this.subscriptions.push(destination);
      } else {
        console.log('Already Subscribed' + destination);
      }
    } else {
      console.log('No Active Connection');
    }
  }

  unsubscribe(destination): void {
    let index = this.subscriptions.indexOf(destination);
    if (this.isConnected) {
      if (index != -1) {
        this.stompClient.unsubscribe(destination);
        this.subscriptions.splice(index, 1);
      } else {
        console.log('Not Subscribed');
      }
    } else {
      console.log('No Active Connection');
    }
  }

  unsubscribeAll(): void {
    if (this.isConnected) {
      this.subscriptions.forEach((subscription) => {
        this.unsubscribe(subscription);
      });
    } else {
      console.log('No Active Connection');
    }
  }
}
