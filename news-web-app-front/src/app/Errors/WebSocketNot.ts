class WebSocketError extends Error{

  constructor(message) {
    super(message);
    this.name = "WebSocketError";
  }
}
