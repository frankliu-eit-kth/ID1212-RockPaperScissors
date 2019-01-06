@project: ID1212-RockPaperScissors

@author: Liming Liu

@contact: limingl@kth.se

@purpose of the project:
	This project is the final project of KTH course ID1212: Network Programming
	In this project I implemented a p2p network using message queue communication paradigm, upon which I built a multiple player rock-paper-scissors game.
	
@Code source & license: 
	 In view part I borrowed some code from previous homework examples, majorly intended to maintain console in/output
	 Here's the license of original code provided by the course lecturer Leif:
	 /*
			 * The MIT License
			 *
			 * Copyright 2017 Leif Lindbäck <leifl@kth.se>.
			 *
			 * Permission is hereby granted, free of charge, to any person obtaining a copy
			 * of this software and associated documentation files (the "Software"), to deal
			 * in the Software without restriction, including without limitation the rights
			 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
			 * copies of the Software, and to permit persons to whom the Software is
			 * furnished to do so, subject to the following conditions:
			 *
			 * The above copyright notice and this permission notice shall be included in
			 * all copies or substantial portions of the Software.
			 *
			 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
			 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
			 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
			 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
			 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
			 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
			 * THE SOFTWARE.
			 */
	 /*
	
	I used message queue for communication, the behavior of which is defined by Java Message Service APIs. In my project I used a open source implementation called OpenJMS, here's the website where I downloaded the resource :http://openjms.sourceforge.net/ 

@How to run:
	(see in folder User guide)

@Code structure:
	In general the project is managed in a traditional MVC structure:
	
	View: View package
	
	Controller: game.adaptor   game.controller   net.controller   net.adaptor
	
	model: game.model  net.model
	
@Control flow:
	(see class diagram in UML folder for better understanding)
	
	The control flow is in general like:
	
	->Read user command from view
	-> interpret the command
	-> action by network controller (create new node/ send message)
	-> wait for message
	-> receive message
	-> pass the message to view level for handling( specifically by GameMsgHandlerImpl instance)
	-> view level reads the new message
	-> action by game controller
	-> pass game result to view layer for console display
	

	
	