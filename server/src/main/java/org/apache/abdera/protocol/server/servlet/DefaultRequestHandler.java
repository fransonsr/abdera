/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  The ASF licenses this file to You
* under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.  For additional information regarding
* copyright in this work, please see the NOTICE file in the top level
* directory of this distribution.
*/
package org.apache.abdera.protocol.server.servlet;

import org.apache.abdera.protocol.server.provider.AbstractResponseContext;
import org.apache.abdera.protocol.server.provider.EmptyResponseContext;
import org.apache.abdera.protocol.server.provider.Provider;
import org.apache.abdera.protocol.server.provider.RequestContext;
import org.apache.abdera.protocol.server.provider.ResponseContext;
import org.apache.abdera.protocol.server.provider.TargetType;

public class DefaultRequestHandler 
  extends AbstractRequestHandler
  implements RequestHandler {

  protected ResponseContext process(
    Provider provider, 
    RequestContext request) {
      String method = request.getMethod().intern();
      TargetType type = request.getTarget().getType();
      if (method == "GET") {
        if (type == TargetType.TYPE_SERVICE) {
          return provider.getService(request, true);
        }
        if (type == TargetType.TYPE_COLLECTION) {
          return provider.getFeed(request, true);
        }
        if (type == TargetType.TYPE_ENTRY) {
          return provider.getEntry(request, true, false);
        }
        if (type == TargetType.TYPE_ENTRY_EDIT) {
          return provider.getEntry(request, true, true);
        }
        if (type == TargetType.TYPE_MEDIA) {
          return provider.getMedia(request, true, false);
        }
        if (type == TargetType.TYPE_MEDIA_EDIT) {
          return provider.getMedia(request, true, true);
        }
      }
      else if (method == "HEAD") {
        if (type == TargetType.TYPE_SERVICE) {
          return provider.getService(request, false);
        }
        if (type == TargetType.TYPE_COLLECTION) {
          return provider.getFeed(request, false);
        }
        if (type == TargetType.TYPE_ENTRY) {
          return provider.getEntry(request, false, false);
        }
        if (type == TargetType.TYPE_ENTRY_EDIT) {
          return provider.getEntry(request, false, true);
        }
        if (type == TargetType.TYPE_MEDIA) {
          return provider.getMedia(request, false, false);
        }
        if (type == TargetType.TYPE_MEDIA_EDIT) {
          return provider.getMedia(request, false, true);
        }
      }
      else if (method == "POST") {
        if (type == TargetType.TYPE_COLLECTION) {
          return provider.createEntry(request);
        }
        if (type == TargetType.TYPE_ENTRY_EDIT) {
          return provider.entryPost(request);
        }
        if (type == TargetType.TYPE_MEDIA_EDIT) {
          return provider.mediaPost(request);
        }
      }
      else if (method == "PUT") {
        if (type == TargetType.TYPE_ENTRY_EDIT) {
          return provider.updateEntry(request);
        }
        if (type == TargetType.TYPE_MEDIA_EDIT) {
          return provider.updateMedia(request);
        }
      }
      else if (method == "DELETE") {
        if (type == TargetType.TYPE_ENTRY_EDIT) {
          return provider.deleteEntry(request);
        }
        if (type == TargetType.TYPE_MEDIA_EDIT) {
          return provider.deleteMedia(request);
        }
      } 
      else if (method == "OPTIONS") {
        AbstractResponseContext rc = new EmptyResponseContext(200);
        rc.addHeader("Allow", combine(getAllowedMethods(type)));
        return rc;
      }
      return null;
  }
  
  protected String[] getAllowedMethods(TargetType type) {
    if (type == null)                       return new String[0];
    if (type == TargetType.TYPE_COLLECTION) return new String[] { "GET", "POST", "HEAD", "OPTIONS" };
    if (type == TargetType.TYPE_ENTRY)      return new String[] { "GET", "HEAD", "OPTIONS" };
    if (type == TargetType.TYPE_MEDIA)      return new String[] { "GET", "HEAD", "OPTIONS" };
    if (type == TargetType.TYPE_ENTRY_EDIT) return new String[] { "GET", "DELETE", "PUT", "POST", "HEAD", "OPTIONS" };
    if (type == TargetType.TYPE_MEDIA_EDIT) return new String[] { "GET", "DELETE", "PUT", "POST", "HEAD", "OPTIONS" };
    if (type == TargetType.TYPE_SERVICE)    return new String[] { "GET", "HEAD", "OPTIONS" };
    return new String[] { "GET", "HEAD", "OPTIONS" };
  }
  
}