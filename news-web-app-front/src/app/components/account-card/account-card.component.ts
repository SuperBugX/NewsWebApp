import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/Authentication/authentication.service';

@Component({
  selector: 'app-account-card',
  templateUrl: './account-card.component.html',
  styleUrls: ['./account-card.component.css'],
})
export class AccountCardComponent implements OnInit {
  isLoggedIn: boolean;
  username: string;

  constructor(private authenticationService: AuthenticationService) {
    this.username = this.authenticationService.getUsername();

    this.authenticationService.loggedIn.subscribe((value) => {
      this.isLoggedIn = value;
    });
  }

  ngOnInit(): void {}

  //Log out the user
  logOut(): void {
    this.authenticationService.logOut();
  }
}
