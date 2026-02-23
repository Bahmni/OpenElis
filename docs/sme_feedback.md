00:15:19.000 --> 00:15:26.000
Vishal Karmalkar: But, uh, I think the bigger, uh, question are the ones that I didn't understand… so Bahmni reports…

00:15:26.000 --> 00:15:31.000
Vishal Karmalkar: does a direct query to, uh, CLI and Clean Limbs database.

00:15:30.000 --> 00:15:32.000
Angshuman Sarkar: Database, yeah.

00:15:31.000 --> 00:15:37.000
Vishal Karmalkar: So we will continue with the same approach, because the architecture diagram as, uh…

00:15:37.000 --> 00:15:44.000
Vishal Karmalkar: uh… this lady came out. Seems like, for Bahmni reports, there is a DirectDB kind of mechanism being used.

00:15:44.000 --> 00:15:51.000
Vishal Karmalkar: In many different cases, right? We go to OpenMRS also via DirectDB. Uh, we go to Udo also via DirectDB, seems like.

00:15:52.000 --> 00:15:54.000
Angshuman Sarkar: Uh, for the, uh…

00:15:52.000 --> 00:15:54.000
Vishal Karmalkar: So…

00:15:54.000 --> 00:15:56.000
Vishal Karmalkar: Bahmni reports.

00:15:56.000 --> 00:16:01.000
Angshuman Sarkar: For the Bahmni reports, Bahmni report does have some level of abstractions.

00:16:01.000 --> 00:16:13.000
Angshuman Sarkar: Alright, uh… so, for example, it provides a bunch of things which you call as a genetic reports, right? So you can say, I know how to fetch the data, this is my data set.

00:16:13.000 --> 00:16:20.000
Angshuman Sarkar: If you want to filter or get certain sort of reports, you can easily do that. Here are the configurations you put in as such.

00:16:20.000 --> 00:16:22.000
Vishal Karmalkar: Yeah.

00:16:20.000 --> 00:16:26.000
Angshuman Sarkar: Uh, we also provide, for example, the Bahmni report was our idea initially.

00:16:26.000 --> 00:16:30.000
Angshuman Sarkar: That, Dejo, we can't solve everybody's reporting problems.

00:16:30.000 --> 00:16:32.000
Vishal Karmalkar: Yep.

00:16:31.000 --> 00:16:35.000
Angshuman Sarkar: Alright, um, but can you build sort of, like, say, essential.

00:16:36.000 --> 00:16:43.000
Angshuman Sarkar: you know, operational reporting framework, through which people, if they want SQL, for example, that's why the SQL, uh.

00:16:44.000 --> 00:16:49.000
Angshuman Sarkar: As such. Uh, we had another one, again, I don't think, like, which was powerful.

00:16:44.000 --> 00:16:46.000
Vishal Karmalkar: Yeah.

00:16:50.000 --> 00:16:56.000
Angshuman Sarkar: But, uh, I don't think, like, you know, we have really gone ahead with this, right? Um, so…

00:16:56.000 --> 00:17:00.000
Angshuman Sarkar: Often the reports, no, are not straightforward.

00:17:00.000 --> 00:17:10.000
Angshuman Sarkar: You do need to, um, crunch separate, separate queries, prepare this data, for example, then assemble together into one unified report.

00:17:10.000 --> 00:17:12.000
Vishal Karmalkar: Yep.

00:17:11.000 --> 00:17:15.000
Angshuman Sarkar: Right, so we created something called as an aggregate query.

00:17:15.000 --> 00:17:23.000
Angshuman Sarkar: report. What does it? Aggregate query report says, basically, like, listen, this report requires data from.

00:17:24.000 --> 00:17:30.000
Angshuman Sarkar: four different, like, you know, themes, right? Doesn't need to be even on the same database.

00:17:26.000 --> 00:17:28.000
Vishal Karmalkar: Just, yeah.

00:17:31.000 --> 00:17:38.000
Angshuman Sarkar: four different, like, say, themes, and then, once you fetch them, for example, you want to merge them together.

00:17:39.000 --> 00:17:45.000
Angshuman Sarkar: So imagine parallel executing four other, like, you know, SQL statements, getting that data, for example.

00:17:45.000 --> 00:17:50.000
Angshuman Sarkar: and then saying, now I'll be able to merge them onto a single reporting template.

00:17:50.000 --> 00:17:56.000
Vishal Karmalkar: Okay. And here you say SQL as structured query language, or CQL, or Clinical Query Language pada?

00:17:51.000 --> 00:17:53.000
Angshuman Sarkar: Right?

00:17:56.000 --> 00:18:02.000
Angshuman Sarkar: No, no, no, no, we don't use the CQL… we do not have the FHIR server.

00:17:58.000 --> 00:18:00.000
Vishal Karmalkar: Okay.

00:18:01.000 --> 00:18:03.000
Vishal Karmalkar: Okay.

00:18:03.000 --> 00:18:11.000
Angshuman Sarkar: That's another one. We should probably talk about it, because I do have certain thoughts about it.

00:18:10.000 --> 00:18:15.000
Vishal Karmalkar: Seems like some generic query language that can be used or something.

00:18:13.000 --> 00:18:25.000
Angshuman Sarkar: Yeah, yeah, yeah. Like, I told you that, like, you know, clinical decision support, man, right now, ours is more about… I posted that ours is more like a request and response, man. Sorry, data hits down.

00:18:19.000 --> 00:18:21.000
Vishal Karmalkar: Yes, okay.

00:18:25.000 --> 00:18:34.000
Angshuman Sarkar: Than this, right? But then, obviously, it's not performant because often, to analyze a patient, this thing, I will need a lot of data, I can't wait on it.

00:18:34.000 --> 00:18:36.000
Vishal Karmalkar: No.

00:18:34.000 --> 00:18:38.000
Angshuman Sarkar: I'd rather, for example, I would think about…

00:18:38.000 --> 00:18:47.000
Angshuman Sarkar: that the other system gets notification, and it keeps on accumulating the data, keeps on processing this data, then I say, okay.

00:18:47.000 --> 00:18:52.000
Angshuman Sarkar: What are your current recommendation for this patient? And then it tells me that.

00:18:51.000 --> 00:18:53.000
Vishal Karmalkar: Hmm.

00:18:52.000 --> 00:19:00.000
Angshuman Sarkar: Right, or we figure out some way, okay? I'll tell you that, or whatever, right?

00:19:01.000 --> 00:19:02.000
Vishal Karmalkar: Thank you.

00:19:02.000 --> 00:19:08.000
Angshuman Sarkar: So that's one thing, so I think that otherwise, it goes to the reports. Now.

00:19:09.000 --> 00:19:15.000
Angshuman Sarkar: Here, you can expose the report, for example, in different ways. You would have seen that on the reporting side, right?

00:19:15.000 --> 00:19:21.000
Vishal Karmalkar: No, I'm not actually explored Bahmni reports at all. It detected that it gets it from OpenAllis, but…

00:19:21.000 --> 00:19:23.000
Vishal Karmalkar: I haven't looked at Bahmni reports at all.

00:19:22.000 --> 00:19:27.000
Angshuman Sarkar: Huh, so most of the reports that we get from the OpenLS would be purely by the SQL.

00:19:28.000 --> 00:19:32.000
Angshuman Sarkar: And these are not that, like, you know, complicated reports. We're not doing…

00:19:30.000 --> 00:19:35.000
Vishal Karmalkar: Okay, we continue the same model in the newer OEG to the Deg Napadega.

00:19:34.000 --> 00:19:35.000
Angshuman Sarkar: Huh.

00:19:35.000 --> 00:19:40.000
Vishal Karmalkar: You're saying, the data model hasn't changed as much, but we can continue the same approach.

00:19:40.000 --> 00:19:47.000
Angshuman Sarkar: Continue the same approach. And operational report, like, you know, most of the time, we serve through Bahmni reports. We don't do…

00:19:45.000 --> 00:19:47.000
Vishal Karmalkar: to.

00:19:47.000 --> 00:19:50.000
Angshuman Sarkar: Too much of a clinical reporting.

00:19:49.000 --> 00:19:59.000
Vishal Karmalkar: Yep. Oh, that is fine. So then, the other question I had asked is, are there other atom feed interactions in Bahmni OpenLS that I need to worry about?

00:19:50.000 --> 00:19:52.000
Angshuman Sarkar: Oh, uh…

00:19:59.000 --> 00:20:04.000
Vishal Karmalkar: Uh, patient sync, we said we would want it to be synced, so I've still not explored.

00:20:04.000 --> 00:20:11.000
Angshuman Sarkar: So there are three… I said, like, 3 categories of data we get, no? One is the metadata.

00:20:04.000 --> 00:20:06.000
Vishal Karmalkar: How was this?

00:20:11.000 --> 00:20:16.000
Angshuman Sarkar: for example, um, OE test, like, for example.

00:20:14.000 --> 00:20:19.000
Vishal Karmalkar: The test catalog, you mean, because…

00:20:17.000 --> 00:20:19.000
Angshuman Sarkar: Huh.

00:20:20.000 --> 00:20:25.000
Vishal Karmalkar: this guy, um, Satish was not sure, but it pointed out, okay, there is a test catalog.

00:20:25.000 --> 00:20:32.000
Vishal Karmalkar: that goes from OpenList to Udu, apparently. Are you… are you aware of that? But Satish was like.

00:20:32.000 --> 00:20:35.000
Angshuman Sarkar: No, no, no, we used to do that before.

00:20:34.000 --> 00:20:36.000
Vishal Karmalkar: Gotcha.

00:20:35.000 --> 00:20:40.000
Angshuman Sarkar: Right? So we don't, uh, do it, uh, anymore.

00:20:40.000 --> 00:20:42.000
Vishal Karmalkar: Okay.

00:20:40.000 --> 00:20:45.000
Angshuman Sarkar: Um, I don't know whether it's a test catalog, but one of the things that we used to do.

00:20:44.000 --> 00:20:52.000
Vishal Karmalkar: Because it found this flow that there is a lab test catalog sync between OpenLS and Udo Connect.

00:20:47.000 --> 00:20:49.000
Angshuman Sarkar: Hmm.

00:20:53.000 --> 00:20:55.000
Vishal Karmalkar: But…

00:20:53.000 --> 00:20:57.000
Angshuman Sarkar: So that you wish to do before, for example.

00:20:57.000 --> 00:21:03.000
Angshuman Sarkar: Okay, so without an order also, somebody could go… can go to the lab.

00:21:03.000 --> 00:21:05.000
Vishal Karmalkar: Oh, at all.

00:21:03.000 --> 00:21:07.000
Angshuman Sarkar: And that, for example, will need to go to the UDU.

00:21:07.000 --> 00:21:11.000
Angshuman Sarkar: Right? Now, over time, we realized, like, was…

00:21:11.000 --> 00:21:20.000
Angshuman Sarkar: you know, then it becomes, like, you know, we have to introduce a specific master data system. Without which and all, we can't, like, you know, solve this.

00:21:20.000 --> 00:21:22.000
Vishal Karmalkar: Correct.

00:21:20.000 --> 00:21:30.000
Angshuman Sarkar: Right? So what do we say, Disney, as for example, right now, for most of the thing, OpenMRS, for example, acts as our, like, in a metadata management system.

00:21:30.000 --> 00:21:32.000
Vishal Karmalkar: Meta Management. Okay.

00:21:31.000 --> 00:21:36.000
Angshuman Sarkar: Alright? So, now, what we have done is this.

00:21:37.000 --> 00:21:43.000
Angshuman Sarkar: Let's say they get a test… a sample drawn without an order.

00:21:44.000 --> 00:21:46.000
Vishal Karmalkar: Okay.

00:21:45.000 --> 00:21:49.000
Angshuman Sarkar: Right? So what do you do? We back create an order onto the OpenMRS.

00:21:49.000 --> 00:21:52.000
Vishal Karmalkar: Oh, so you need bi-directionally, okay.

00:21:51.000 --> 00:21:58.000
Angshuman Sarkar: Alright, so we back create an order from the OpenMRS. Now, as soon as we backcreate an order onto OpenMRS.

00:21:58.000 --> 00:22:01.000
Angshuman Sarkar: Udo now knows there is an order being created.

00:22:02.000 --> 00:22:10.000
Angshuman Sarkar: So, we didn't have to do any other work onto that. And also, the subsequent results, for example, are against that order.

00:22:06.000 --> 00:22:08.000
Vishal Karmalkar: Oh.

00:22:10.000 --> 00:22:12.000
Vishal Karmalkar: Yeah.

00:22:10.000 --> 00:22:14.000
Angshuman Sarkar: Alright, so it, like, you know, eases things for us that way.

00:22:14.000 --> 00:22:25.000
Vishal Karmalkar: Okay, so what are the… so, PatientSync… it found out patient sync test catalog and metadata as the current atom feed interactions.

00:22:25.000 --> 00:22:28.000
Vishal Karmalkar: Between OpenLS and OpenMRS, but is there…

00:22:27.000 --> 00:22:35.000
Angshuman Sarkar: EMR to OpenList, that's what we send out, right? Basically, the metadata, including all the tests and everything else.

00:22:34.000 --> 00:22:36.000
Vishal Karmalkar: Hmm.

00:22:35.000 --> 00:22:41.000
Angshuman Sarkar: some things… you can say it's part of the metadata, we also indicate that.

00:22:41.000 --> 00:22:49.000
Angshuman Sarkar: what is part of what sample type, etc, right? Oh, for example, if you think about, say, leukocyte, right? Leukocyte can be done.

00:22:49.000 --> 00:22:55.000
Angshuman Sarkar: against blood can be done about cerebral spinal fluid, multiple samples, right? So…

00:22:55.000 --> 00:23:02.000
Angshuman Sarkar: Uh, so that references an associated metadata that it creates, and other than, it's a patient, and eventually it's an order.

00:23:02.000 --> 00:23:06.000
Angshuman Sarkar: Alright, test out of a specific type.

00:23:05.000 --> 00:23:08.000
Vishal Karmalkar: Yeah, that's the… that's the main one is there. Okay, good.

00:23:07.000 --> 00:23:12.000
Angshuman Sarkar: While reversing, only thing that we get, for example.

00:23:12.000 --> 00:23:14.000
Angshuman Sarkar: Normal cases, we get the results.

00:23:14.000 --> 00:23:16.000
Vishal Karmalkar: Results, correct?

00:23:15.000 --> 00:23:25.000
Angshuman Sarkar: The other case, for example, we will get a accession, or the sample drawn, based on which we create a corresponding order.

00:23:27.000 --> 00:23:31.000
Vishal Karmalkar: That is when somebody went to, uh, would…

00:23:29.000 --> 00:23:32.000
Angshuman Sarkar: Lab without an order directly.

00:23:31.000 --> 00:23:34.000
Vishal Karmalkar: Not without an order, okay, take it.

00:23:34.000 --> 00:23:43.000
Vishal Karmalkar: Okay, uh, these two were a lot more, uh, technical questions. We can unpack them right now, but…

00:23:42.000 --> 00:23:49.000
Angshuman Sarkar: I can tell you this, like, you know, uh… yeah. So, what he's saying is this, right, um…

00:23:43.000 --> 00:23:45.000
Vishal Karmalkar: I'm sorry.

00:23:51.000 --> 00:24:04.000
Angshuman Sarkar: that… I need to check about how OpenLS is, like, you know, getting that. I'm expecting, for example, they're getting a bundle. So think about a bundle, it's like a collection of.

00:24:04.000 --> 00:24:13.000
Angshuman Sarkar: different resources, all right? What they're saying is this, in the bundle, most likely that will be the case. I need to know about who's the patient.

00:24:06.000 --> 00:24:07.000
Vishal Karmalkar: Okay. Yeah.

00:24:13.000 --> 00:24:15.000
Angshuman Sarkar: Obviously. I need to know…

00:24:15.000 --> 00:24:17.000
Angshuman Sarkar: What is the order?

00:24:17.000 --> 00:24:20.000
Angshuman Sarkar: Alright, and probably some task.

00:24:20.000 --> 00:24:22.000
Vishal Karmalkar: No.

00:24:21.000 --> 00:24:27.000
Angshuman Sarkar: Right? Now, what they're saying, when a task, for example, I don't know why it is asking for at that point of time.

00:24:27.000 --> 00:24:29.000
Vishal Karmalkar: Hmm.

00:24:28.000 --> 00:24:34.000
Angshuman Sarkar: It is saying that task.owner must match OAG's remote source identifier. Ah!

00:24:34.000 --> 00:24:37.000
Angshuman Sarkar: Okay. So, basically.

00:24:38.000 --> 00:24:40.000
Angshuman Sarkar: Imagine I am…

00:24:41.000 --> 00:24:43.000
Angshuman Sarkar: Sending out an order.

00:24:43.000 --> 00:24:50.000
Angshuman Sarkar: Right? And I'm creating a task associated to this.

00:24:50.000 --> 00:24:52.000
Angshuman Sarkar: This is, like, you know, I have created.

00:24:52.000 --> 00:24:56.000
Angshuman Sarkar: How does… imagine you have 4 labs.

00:24:55.000 --> 00:24:57.000
Vishal Karmalkar: Yeah.

00:24:57.000 --> 00:25:00.000
Angshuman Sarkar: How does each lab identify merely API?

00:25:00.000 --> 00:25:02.000
Vishal Karmalkar: Correct.

00:25:01.000 --> 00:25:03.000
Angshuman Sarkar: Right? So that's what the…

00:25:04.000 --> 00:25:09.000
Angshuman Sarkar: owner, for example, like, you know, we'll be probably done. And here, for example.

00:25:09.000 --> 00:25:17.000
Angshuman Sarkar: remote.source.identifier is this, is saying, who is the source? Who has sent me? So, if I have 5 labs, for example.

00:25:17.000 --> 00:25:22.000
Angshuman Sarkar: or 5 clinics which have sent me out, for example, I need to know that who has sent me out.

00:25:19.000 --> 00:25:21.000
Vishal Karmalkar: Got it.

00:25:22.000 --> 00:25:24.000
Vishal Karmalkar: Got it.

00:25:22.000 --> 00:25:26.000
Angshuman Sarkar: Because I am processing theirs, and eventually, when I…

00:25:27.000 --> 00:25:31.000
Angshuman Sarkar: I need to say, this was your ask, and this is, like, you know, what you're going to get.

00:25:29.000 --> 00:25:31.000
Vishal Karmalkar: Hmm.

00:25:31.000 --> 00:25:43.000
Vishal Karmalkar: Right, so that's kind of how I took it, Ki. Some almost sort of an authentication kind of thing at the end of the day. And that was… that should be the only system that gets the results back as well.

00:25:39.000 --> 00:25:41.000
Angshuman Sarkar: Yeah.

00:25:43.000 --> 00:25:45.000
Vishal Karmalkar: You know? So…

00:25:43.000 --> 00:25:48.000
Angshuman Sarkar: Right. Now, in our case, we probably have to investigate.

00:25:48.000 --> 00:25:50.000
Angshuman Sarkar: Our case, think about…

00:25:51.000 --> 00:25:56.000
Angshuman Sarkar: Although, we are thinking about a location as a clinic.

00:25:56.000 --> 00:25:58.000
Angshuman Sarkar: Right, as such.

00:25:59.000 --> 00:26:03.000
Angshuman Sarkar: Is it going to be enough doing that way?

00:26:02.000 --> 00:26:04.000
Vishal Karmalkar: Yeah.

00:26:03.000 --> 00:26:05.000
Angshuman Sarkar: And it might be also.

00:26:06.000 --> 00:26:08.000
Angshuman Sarkar: Or, uh…

00:26:08.000 --> 00:26:13.000
Angshuman Sarkar: does it identify it as a system? Because if it identifies as a system, for example.

00:26:14.000 --> 00:26:17.000
Angshuman Sarkar: Then was meratu bus eki sistan.

00:26:16.000 --> 00:26:24.000
Vishal Karmalkar: Because your IPD, OPD, Room 1, Room 2 is immaterial to how it is set up in OpenMR, is something.

00:26:24.000 --> 00:26:26.000
Angshuman Sarkar: Right, right.

00:26:24.000 --> 00:26:33.000
Vishal Karmalkar: It is just coming from this system, basically, one system, because we don't support multiple systems right now, any case, so, yeah.

00:26:28.000 --> 00:26:30.000
Angshuman Sarkar: Exactly, right.

00:26:33.000 --> 00:26:36.000
Angshuman Sarkar: Correct. Now, in IOM's case.

00:26:34.000 --> 00:26:36.000
Vishal Karmalkar: 2.

00:26:36.000 --> 00:26:39.000
Angshuman Sarkar: We do have, sort of, like.

00:26:38.000 --> 00:26:40.000
Vishal Karmalkar: You have that requirement of multi…

00:26:39.000 --> 00:26:46.000
Angshuman Sarkar: office? Yeah, because each one is a clinic. Although they are running on the same OpenMRS, same Bahmni.

00:26:46.000 --> 00:26:49.000
Angshuman Sarkar: Through login, they're logging onto a clinic.

00:26:50.000 --> 00:26:55.000
Angshuman Sarkar: Unlike, like, say, if you would have seen Bahmni default, you're logging onto a location within a clinic.

00:26:55.000 --> 00:26:57.000
Vishal Karmalkar: Correct, correct, got it.

00:26:57.000 --> 00:27:05.000
Vishal Karmalkar: Yep. I understand. So, I've kept this out of scope, that multi-location, multi-clinic thing any case, because that's…

00:26:59.000 --> 00:27:01.000
Angshuman Sarkar: Alright, so that…

00:27:03.000 --> 00:27:05.000
Angshuman Sarkar: Oh, hmm.

00:27:05.000 --> 00:27:13.000
Vishal Karmalkar: I think a bigger question. Yeah, so we'll have to understand care default value, assuming one clinic only at this point.

00:27:05.000 --> 00:27:08.000
Angshuman Sarkar: Yeah, so that's the meaning of this, huh?

00:27:12.000 --> 00:27:14.000
Angshuman Sarkar: Hmm.

00:27:13.000 --> 00:27:17.000
Vishal Karmalkar: This one is also slightly complicated, I didn't fully get it.

00:27:17.000 --> 00:27:22.000
Angshuman Sarkar: So, basically, like, you know, we have the lab order results, for example, uh…

00:27:22.000 --> 00:27:25.000
Angshuman Sarkar: Uh, uh, right?

00:27:25.000 --> 00:27:30.000
Angshuman Sarkar: And… where is this lab order results? So…

00:27:31.000 --> 00:27:36.000
Angshuman Sarkar: I don't think this is, like, you know, uh, that important, right?

00:27:36.000 --> 00:27:41.000
Angshuman Sarkar: Because the way that we used to store the data, right.

00:27:41.000 --> 00:27:46.000
Angshuman Sarkar: Uh, the data structure, if you remember, like, you know, I think once I spoke that why we are doing.

00:27:46.000 --> 00:27:51.000
Angshuman Sarkar: Uh, this, right? All the diagnostic, uh, report APIs, as such.

00:27:52.000 --> 00:27:54.000
Angshuman Sarkar: So is to store the…

00:27:54.000 --> 00:28:06.000
Angshuman Sarkar: data, for example, in one data structure. And that was really bad for performance and other, uh, this thing-wise, right? There is no easy way to query, no easy way to find out, nothing.

00:28:01.000 --> 00:28:03.000
Vishal Karmalkar: Okay, okay.

00:28:06.000 --> 00:28:10.000
Angshuman Sarkar: So now we have moved on to a different, uh…

00:28:10.000 --> 00:28:12.000
Angshuman Sarkar: Like, say, uh…

00:28:13.000 --> 00:28:16.000
Angshuman Sarkar: Um, data structure all together.

00:28:16.000 --> 00:28:26.000
Angshuman Sarkar: All right, now internal representation may be still different, but you can say this is only going to get easier because both the models are fire aligned.

00:28:25.000 --> 00:28:28.000
Vishal Karmalkar: Fire compatible now. Yep.

00:28:27.000 --> 00:28:32.000
Angshuman Sarkar: Right? When MRS is reading from, like, say.

00:28:32.000 --> 00:28:38.000
Angshuman Sarkar: OpenLS, it is for Fired server, it is giving me a FHIR diagnostic report, and in this case.

00:28:33.000 --> 00:28:35.000
Vishal Karmalkar: Yep.

00:28:37.000 --> 00:28:39.000
Vishal Karmalkar: Got it.

00:28:38.000 --> 00:28:43.000
Angshuman Sarkar: Also, every model, for example, is aligned to Aspire.

00:28:42.000 --> 00:28:50.000
Vishal Karmalkar: Correct, that's… Satish kind of answered it that way, if there are gaps, then Bahmni has to change, because we are moving towards the standard, so this will just…

00:28:50.000 --> 00:28:52.000
Angshuman Sarkar: Exactly.

00:28:50.000 --> 00:28:53.000
Vishal Karmalkar: Make it… make it easier than at that point.

00:28:52.000 --> 00:28:57.000
Angshuman Sarkar: So this should be okay. I don't think they will go by the older one.

00:28:57.000 --> 00:28:59.000
Vishal Karmalkar: No.

00:28:57.000 --> 00:29:05.000
Angshuman Sarkar: But… does it provide any other thing? Because I'm wondering why is it mentioning minnormal, max normal?

00:29:07.000 --> 00:29:09.000
Angshuman Sarkar: Mmm…

00:29:10.000 --> 00:29:12.000
Angshuman Sarkar: So I'll tell you what, uh…

00:29:12.000 --> 00:29:17.000
Angshuman Sarkar: there is certain deficiency, and I'm wondering whether it is hinting towards that.

00:29:18.000 --> 00:29:20.000
Angshuman Sarkar: Right, um…

00:29:21.000 --> 00:29:23.000
Angshuman Sarkar: So, imagine…

00:29:21.000 --> 00:29:29.000
Vishal Karmalkar: And take care of this, when we get to it also, we will explore this more, so I'm okay to park this if it doesn't change the design drastically.

00:29:26.000 --> 00:29:28.000
Angshuman Sarkar: Sure. Sure.

00:29:29.000 --> 00:29:31.000
Angshuman Sarkar: Hmm.

00:29:29.000 --> 00:29:34.000
Vishal Karmalkar: We'll figure it out. So, okay, so this is good. So what I have… I have…

00:29:34.000 --> 00:29:40.000
Vishal Karmalkar: understood the plan at a high level also. It's saying, start slow, start manual, you know, spin up OEG to.

00:29:40.000 --> 00:29:52.000
Vishal Karmalkar: make sure the external FHIR API, you know, returns everything. So I'll start working through this. Uh, I think today you have, uh, unblocked me for sometimes, that's… that's fine.

00:29:40.000 --> 00:29:42.000
Angshuman Sarkar: Hmm.

00:29:47.000 --> 00:29:49.000
Angshuman Sarkar: Hmm.

00:29:52.000 --> 00:30:01.000
Vishal Karmalkar: I just keep on making progress. But yes, if there is the complete other thing that we have to explore, then I told the same thing to Satish. You guys know the domain better, or.



