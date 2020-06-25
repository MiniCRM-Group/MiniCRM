import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { IntroRoutingModule } from './intro-routing.module';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { IntroComponent } from './intro.component';


@NgModule({
  declarations: [LandingPageComponent, LoginPageComponent, IntroComponent],
  imports: [
    CommonModule,
    IntroRoutingModule
  ]
})
export class IntroModule { }
