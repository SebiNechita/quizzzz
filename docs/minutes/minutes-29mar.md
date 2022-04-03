# Minutes

**Opening by chairperson**\
_No minutes._

**Check in**\
Ilia: importing activity. admin panel, jokers
Kim: lobby, improved connection page
Sebastian: multiplayer, pop up when closing the window
Sander: javadoc and tests. helped Kim with the lobby.   Help Ilia and Sebastian with their tasks.
Kristof: importing activity. Worked on another question type.
Kaushik: user page. improve help page. add sound effects

**Approval of the agenda**\
Everyone agrees.

**Discussed Points**

- **Are we on track in order to complete all issues?**
    * Have we completed all the must-haves?\
      two must-haves are still open.
      readme file(how to run the application)
      see leaderboard at the end of a game.
    * Will we complete all the should-haves?\
      8 could-haves left. should be feasible in this sprint.
    * What could-haves will we do and which will we set aside?
- **Feedback**
    * Feedback from Jegor about planning, task distribution, reviews, etc.\
      planning:\
      we could have definition-of-done header.
      some issues could be splitted\
      taks-distribution:\
      Kristof’s issues had less weight.
      Kim and Ilia’s issues had more weight.
      Ilia should finish implementing the jokers by tomorrow.\
      weights:\
      weights and milestone is present.
      Kristof need more issue,weight,contribution in this sprint.
      Kristof not having enough commits/code/is alerting.\
      pie chart/gitinspector:\
      more contribution (from people other than Sander and Kaushik)\
      commits:\
      fine.But some commits are too large.
      GameController is too large. Refactor big classes.
      Add more javadocs(for class description)
      testing consists only 25% right now. No test for userservice. important.\
      review:\
      kristof has a open merge request.
      Ilia and Kristof could comment more.
      Sander had too much approval.\
      pipeline:\
      too many failed pipelines.\
      Continuous work from Kristop is expected.
      work on testing.
      anyone other than kaushik and Sander should contribute more.

- **Division of tasks**
    * Who still has to work more and who will have to work less?\
      Ilia and Kim still need contribution this sprint.
    * Is there a significant improvement since last time? What should we continue with and what should we still work on?
    * Who will work on which issues? (Knowing that some people need more contribution than others)\
      Sander could work on refactoring and testing.
      Kristof could work on leaderboard.
      Sebastian could work on multiplayer game.
    
      2 apis have no tests.
      UserService has no tests.
      look at the rubric for detailed information.
      no tests for GUI, but need manual tests.(like a documentation)
      need tests in client side logic. We could separate the logic and GUI and test it.
      Leave websocket class there.
      must-haves are fine.
      point system: keep track of everyone's score and show it in the leaderboard(and store it)






- **Manual testing for the client**
  * We have difficulty testing the client (very few tests currently exist); Jegor mentioned in the last meeting that “Manual testing” is an option, but we do not know yet how to go about doing that.
- **Video at the end of the project**
  * What is expected from us?\
    Video until next week friday.
    Fill in buddycheck meaningfully and work on assignment 5b. Self-reflection can be copied from buddycheck. Work on the final version of code-of-conduct.
    The content of the video should be about flexing the application and shows what the project is about.

next week:
around thursday show physical presentation to Jegor.
Video requirements will be sent to us.


**Action Points**
| Action Item | Responsible member(s) | Deadline |
|-------------|-----------------------|----------|
| implement multiplayer game | Sebastian | This sprint |
| split multiplayer game issue into smaller ones| Sander | This sprint |