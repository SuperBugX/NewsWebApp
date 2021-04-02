class WebSocketNotConnectedError extends WebSocketError{

  constructor(message) {
    super(message);
    this.name = "WebSocketNotConnectedError";
  }
}
