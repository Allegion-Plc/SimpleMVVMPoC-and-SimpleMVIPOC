# SimpleMVVMPoC-and-SimpleMVIPOC
This repo houses two POC (Proof of Concept) applications for the purposes of demonstrating two design patterns. Both applications implement the same requirements of logging a user into the application and retrieving and displaying a list of rights the user account has access to. The applications are heavily commented and are to be used for demonstration purposes only.

# SimpleMVVMPoC
This application implements the requirements using the Model-View-ViewModel pattern which separates application responsibilities into three categories, each specifically concerned with a well defined responsibility

## Model
Object-representations of data required for logic, and classes responsible for retrieving said data

## View
Classes solely focused on displaying information to a user and updating said information as the model updates

## ViewModel
Classes focused on acting as a liasion between the view and the model. These classes expose observable data which anyone can subscribe to for model updates. 

# SimpleMVIPoC
This application implements the requirements by expanding upon the MVVM pattern, adding the intent package.

## Intent
Classes focused on adding state and actions to the application, giving it a more well-defined set of capabilities and giving those capabilities well-defined statuses for their operation
