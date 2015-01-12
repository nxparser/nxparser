package org.semanticweb.yars.nx.namespace;

import org.semanticweb.yars.nx.Resource;

public class SIOC {
	public static final String NS = "http://rdfs.org/sioc/ns#";

	//classes
	public static final Resource COMMUNITY = new Resource(NS+"Community");
	public static final Resource CONTAINER = new Resource(NS+"Container");
	public static final Resource FORUM = new Resource(NS+"Forum");
	public static final Resource ITEM = new Resource(NS+"Item");
	public static final Resource POST = new Resource(NS+"Post");
	public static final Resource ROLE = new Resource(NS+"Role");
	public static final Resource SITE = new Resource(NS+"Site");
	public static final Resource SPACE = new Resource(NS+"Space");
	public static final Resource THREAD = new Resource(NS+"Thread");
	public static final Resource USER = new Resource(NS+"User");
	public static final Resource USERGROUP = new Resource(NS+"Usergroup");
	
	//properties
	public static final Resource ABOUT = new Resource(NS+"about");
	public static final Resource ACCOUNTOF = new Resource(NS+"account_of");
	public static final Resource ADMINISTRATOROF = new Resource(NS+"administrator_of");
	public static final Resource ATTACHMENT = new Resource(NS+"attachment");
	public static final Resource AVATAR = new Resource(NS+"avatar");
	public static final Resource CONTAINEROF = new Resource(NS+"container_of");
	public static final Resource CONTENT = new Resource(NS+"content");
	public static final Resource CREATOROF = new Resource(NS+"creator_of");
	public static final Resource EARLIER_VERSION = new Resource(NS+"earlier_version");
	public static final Resource EMAIL = new Resource(NS+"email");
	public static final Resource EMAILSHA1 = new Resource(NS+"email_sha1");
	public static final Resource FEED = new Resource(NS+"feed");
	public static final Resource FOLLOWS = new Resource(NS+"follows");
	public static final Resource FUNCTIONOF = new Resource(NS+"function_of");
	public static final Resource HASADMINISTRATOR = new Resource(NS+"has_administrator");
	public static final Resource HASCONTAINER = new Resource(NS+"has_container");
	public static final Resource HASCREATOR = new Resource(NS+"has_creator");
	public static final Resource HASDISCUSSION = new Resource(NS+"has_discussion");
	public static final Resource HASFUNCTION = new Resource(NS+"has_function");
	public static final Resource HASHOST = new Resource(NS+"has_host");
	public static final Resource HASMEMBER = new Resource(NS+"has_member");
	public static final Resource HASMODERATOR = new Resource(NS+"has_moderator");
	public static final Resource HASMODIFIER = new Resource(NS+"has_modifier");
	public static final Resource HASOWNER = new Resource(NS+"has_owner");
	public static final Resource HASPARENT = new Resource(NS+"has_parent");
	public static final Resource HASREPLY = new Resource(NS+"has_reply");
	public static final Resource HASSCOPE = new Resource(NS+"has_scope");
	public static final Resource HASSPACE = new Resource(NS+"has_space");
	public static final Resource HASUBSCRIBER = new Resource(NS+"has_subscriber");
	public static final Resource HASUSERGROUP = new Resource(NS+"has_usergroup");
	public static final Resource HOSTOF = new Resource(NS+"host_of");
	public static final Resource ID = new Resource(NS+"id");
	public static final Resource IPADDRESS = new Resource(NS+"ip_address");
	public static final Resource LATERVERSION = new Resource(NS+"later_version");
	public static final Resource LATESTVERSION = new Resource(NS+"latest_version");
	public static final Resource LINK = new Resource(NS+"link");
	public static final Resource LINKSTO = new Resource(NS+"links_to");
	public static final Resource MEMBEROF = new Resource(NS+"member_of");
	public static final Resource MODERATOROF = new Resource(NS+"moderator_of");
	public static final Resource MODIFIEROF = new Resource(NS+"modifier_of");
	public static final Resource NAME = new Resource(NS+"name");
	public static final Resource NEXTBYDATE = new Resource(NS+"next_by_date");
	public static final Resource NEXTVERSION = new Resource(NS+"next_version");
	public static final Resource NOTE = new Resource(NS+"note");
	public static final Resource NUMREPLIES = new Resource(NS+"num_replies");
	public static final Resource NUMVIEWS = new Resource(NS+"num_views");
	public static final Resource OWNEROF = new Resource(NS+"owner_of");
	public static final Resource PARENTOF = new Resource(NS+"parent_of");
	public static final Resource PREVIOUSBYDATE = new Resource(NS+"previous_by_date");
	public static final Resource PREVIOUSVERSION = new Resource(NS+"previous_version");
	public static final Resource RELATEDTO = new Resource(NS+"related_to");
	public static final Resource REPLYOF = new Resource(NS+"reply_of");
	public static final Resource SCOPEOF = new Resource(NS+"scope_of");
	public static final Resource SIBLING = new Resource(NS+"sibling");
	public static final Resource SPACEOF = new Resource(NS+"space_of");
	public static final Resource SUBSCIBEROF = new Resource(NS+"subscriber_of");
	public static final Resource TOPIC = new Resource(NS+"topic");
	public static final Resource USERGROUPOF = new Resource(NS+"usergroup_of");

	public static final Resource RELATED_TO = new Resource(NS+"related_to");

}
