import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from 'rxjs';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {map, shareReplay} from 'rxjs/operators';
import {MatDialog} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {AboutComponent} from '../about/about.component';
import {AuthService} from '../../service/auth.service';
import {User} from '../../model/user.model';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {
  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  @Input() visible = false;
  @Output() toggle: EventEmitter<boolean> = new EventEmitter<boolean>();
  username$: Observable<string>;

  constructor(private breakpointObserver: BreakpointObserver,
              public dialog: MatDialog,
              private authService: AuthService,
              private router: Router) {
    this.username$ = this.authService.currentUser.pipe(map((user: User) => user?.email));
  }

  ngOnInit(): void {
  }

  isAuthenticated(): boolean {
    const currentUser = this.authService.currentUserValue;
    return !!currentUser;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']).then();
  }

  toggleOverlay(event: any): void {
    this.visible = !this.visible;
    this.toggle.emit(this.visible);
  }

  openUsers(): void {
    this.router.navigate(['/users']).then();
  }

  isAdmin(): boolean {
    return this.authService.currentUserValue.role === 'admin';
  }

  openCompanies(): void {
    this.router.navigate(['/companies']).then();
  }

  about(): void {
    this.dialog.open(AboutComponent, {disableClose: false});
  }
}
