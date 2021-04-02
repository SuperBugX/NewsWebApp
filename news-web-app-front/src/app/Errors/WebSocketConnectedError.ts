class WebSocketConnectedError extends WebSocketError{

  constructor(message) {
    super(message);
    this.name = "WebSocketConnectedError";
  }
}
