class WebSocketStompError extends WebSocketError{

  constructor(message) {
    super(message);
    this.name = "WebSocketStompError";
  }
}
