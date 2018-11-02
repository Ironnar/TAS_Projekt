import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { ProductsComponent } from './products/products.component';
import { ServicesComponent } from './services/services.component';
import { AppRoutingModule } from './app-routing.module';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { ServiceDetailComponent } from './service-detail/service-detail.component';
import { MessagesComponent } from './messages/messages.component';
import { MainComponent } from './main/main.component';
import { CategoriesComponent } from './categories/categories.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { SignupFormComponent } from './signup-form/signup-form.component';
import { CategoryComponent } from './category/category.component';
import { ProductFormComponent } from './product-form/product-form.component';
import { ServiceFormComponent } from './service-form/service-form.component';

@NgModule({
  declarations: [
    AppComponent,
    ProductsComponent,
    ServicesComponent,
    ProductDetailComponent,
    ServiceDetailComponent,
    MessagesComponent,
    MainComponent,
    CategoriesComponent,
    LoginFormComponent,
    SignupFormComponent,
    CategoryComponent,
    ProductFormComponent,
    ServiceFormComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    // HttpClientInMemoryWebApiModule.forRoot(
    //   InMemoryDataService, { dataEncapsulation: false }
    // ),
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }