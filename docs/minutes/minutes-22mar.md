# Minutes

**Opening by chairperson**\
_No minutes._

**Check in**\
Ilia: Worked on WebSockets (There was an issue with HTTPS)
Kim: Worked on tests
Sebastian: Worked on the waiting room for multiplayer
Sander: Game screen for open questions
Kristof: Single-player game functionality
Kaushik: Single-player game functionality

**Approval of the agenda**\
Everyone agrees.

**Discussed Points**

- **Discuss the progress of the team**
    * Is everybody on track?
    * Equal contribution:
    * Testing:\
    We have already started but need more
    * Planning and Task distribution:\
    A lot of must-haves still exist, not many could and should haves percentage-wise. Some issues are also on the big side of things. \
    Kim should be assigned to more issues.\
    Issues having weight is something we should continue with.
    * Contribution:\
    This distribution is improving. Kim should not be the only one doing tests. Everyone should have an actual feature of their own.\
    Commits: Some are too big, some are too small. Commits should be focussed on a distinct feature, but also should refrain from only changing one method. This will help oversee commits and can help with rolling back features. \
    More JavaDoc and more tests should be added, manual testing for the client: see the rubric. There can also be tests for added for the logic side. Server-side is lacking tests in general.
    * Reviewing:\
    Most people only have one merge request, try to get two. Don't postpone merging. Kim should commend and approve more. There can be an agreement that a person should review a merge request in a limited amount of time. The build server still fails a lot: try to run the pipeline locally.
- **Discuss the things that we were not able to finish during this sprint**
    * We can use long polling as an alternative to WebSockets. Jegor also recommends long-polling. Secure WebSockets seem complicated and there is also not much information available about the.m
- **Other notable mentions**
    * Issues should be mainly assigned to one person: it is ok if someone helps a bit, but there should be one person who does most of the work.
    * We should make sure that each person has contributed to the client and server: e.g. assign one issue for client and one for server per person. You should have an understanding of each side.
    * Emails will be sent by course staff regarding contribution.
    * This sprint: More contribution from especially Kim, but also Sebastian and Ilia.

**Action Points**
| Action Item | Responsible member(s) | Deadline |
|-------------|-----------------------|----------|
| Upload the documents of this meeting to GitLab | Sander | Next meeting |
