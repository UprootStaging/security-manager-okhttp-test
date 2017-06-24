package com.lavadip.smokhttp;

import java.net.NetPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;

public class LocalSecurityPolicy extends Policy {

	public static Policy getInstance() {
		return new LocalSecurityPolicy();
	}

	@Override
	public PermissionCollection getPermissions(CodeSource codesource) {
	  final PermissionCollection permissions = new Permissions();
	  permissions.add(new NetPermission("getProxySelector"));
	  return permissions;
	}
}
