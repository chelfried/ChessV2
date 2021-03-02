import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {NavigationEnd, Router} from "@angular/router";
import {SseClient} from 'angular-sse-client';
import {delay} from "rxjs/operators";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {

  navSub;

  publicIP = 'http://5.12.12.226:81/api/';

  title = 'Chess';

  publicIpAddr: string;

  gameStarted: boolean;
  playingBlack: boolean;
  gameMessage: string;
  board: string[];
  fieldClass: string[];
  color: string;
  whitePromoting: boolean;
  blackPromoting: boolean;

  constructor(
    private http: HttpClient,
    private router: Router,
    private sseClient: SseClient) {
    this.navSub = this.router.events.subscribe((e: any) => {
      if (e instanceof NavigationEnd) {
      }
    })
  }

  ngOnInit() {
    this.hasGameStarted();
    this.getFieldClasses();
    this.getPieces();
    this.getPlayerColor();
    this.getGameMessage();
    this.isWhitePromoting();
    this.isBlackPromoting();
    this.subscribeToSSE();
  }

  subscribeToSSE() {
    this.sseClient.get(this.publicIP + 'subscribe')
      .subscribe(data => {
        this.hasGameStarted();
        this.getFieldClasses();
        this.getPieces();
        this.getPlayerColor();
        this.getGameMessage();
        this.isWhitePromoting();
        this.isBlackPromoting();
      });
  }

  getPieces() {
    this.http
      .get<string[]>(this.publicIP + 'board')
      .subscribe(data => {
        this.board = data;
      });
  }

  getFieldClasses() {
    this.http
      .get<string[]>(this.publicIP + 'fieldClass')
      .subscribe(data => {
        this.fieldClass = data;
      });
  }

  getPlayerColor() {
    this.http
      .get<boolean>(this.publicIP + 'playingBlack')
      .subscribe(data => {
        this.playingBlack = data;
      });
  }

  isWhitePromoting() {
    this.http
      .get<boolean>(this.publicIP + 'whitePromoting')
      .subscribe(data => {
        this.whitePromoting = data;
      });
  }

  isBlackPromoting() {
    this.http
      .get<boolean>(this.publicIP + 'blackPromoting')
      .subscribe(data => {
        this.blackPromoting = data;
      });
  }

  promote(piece: string) {
    this.http
      .post<string>(this.publicIP + 'promote/' + piece, null)
      .subscribe(() => {
      });
  }

  getGameMessage() {
    this.http
      .get<string>(this.publicIP + 'gameMessage')
      .subscribe(response => {
        delay(200);
        this.gameMessage = response;
      });
  }

  pickColor(color: string) {
    this.http
      .post<string>(this.publicIP + color, null)
      .subscribe(() => {
        delay(100);
        window.location.reload();
      });
  }

  resetBoard() {
    this.http
      .post<string>(this.publicIP + 'reset/', null)
      .subscribe(() => {
        delay(100);
        window.location.reload();
      });
  }

  select(sel: number) {
    this.http
      .post<string>(this.publicIP + sel, null)
      .subscribe(() => {
      });
  }

  hasGameStarted() {
    this.http
      .get<boolean>(this.publicIP + 'gameStarted')
      .subscribe(data => {
        this.gameStarted = data;
      });
  }

}
