import { Component} from '@angular/core';
import { AuthenticationService } from './services/Authentication/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  title : string;
  isLoggedIn : boolean;

  constructor(private authenticationService: AuthenticationService) {
    this.title = "The Hawkers Journal";

    this.authenticationService.loggedIn.subscribe((value) => {
      this.isLoggedIn = value;
    })
  }
}
