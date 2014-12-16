/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2014 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.sonar.server.user;

import org.sonar.api.ServerComponent;
import org.sonar.core.permission.GlobalPermissions;
import org.sonar.server.user.index.UserIndexer;

public class UserService implements ServerComponent {

  private final UserIndexer userIndexer;
  private final UserCreator userCreator;

  public UserService(UserIndexer userIndexer, UserCreator userCreator) {
    this.userIndexer = userIndexer;
    this.userCreator = userCreator;
  }

  public void create(NewUser newUser) {
    checkPermission();
    userCreator.create(newUser);
    userIndexer.index();
  }

  public void index() {
    userIndexer.index();
  }

  private void checkPermission() {
    UserSession userSession = UserSession.get();
    userSession.checkLoggedIn();
    userSession.checkGlobalPermission(GlobalPermissions.SYSTEM_ADMIN);
  }
}
