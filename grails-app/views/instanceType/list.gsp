<%--

    Copyright 2012 Netflix, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <title>Instance Types</title>
</head>
<body>
  <div class="body">
    <h1>Instance types</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
      <div class="buttons"></div>
      <table class="sortable instanceType">
        <tr>
          <th>Name</th>
          <th>Description</th>
          <th>Ram</th>
          <th class="sorttable_nosort" colspan="2">Volumes</th>
         <%--
          <th>Arch</th>
          <th>IO Perf</th>
          <th>Linux<br/>On<br/>Dem</th>
          <th>Linux<br/>Res</th>
          <th>Linux<br/>Spot</th>
          <th>Win<br/>On<br/>Dem</th>
          <th>Win<br/>Res</th>
          <th>Win<br/>Spot</th>
        --%></tr>
				<g:each in="${instanceTypes}" var="instanceType">
					<tr>
						<td>
							${instanceType.hardware.id}
						</td>
						<td class="description">
							${instanceType.hardware.processors}
						</td>
						<td>
							${instanceType.hardware.ram}
						</td>
						<td>
							<table>
								<tr>
									<th>Device</th>
									<th>Size</th>
									<th>BootDevice</th>
									<th>Durable</th>
								</tr>
								<g:each in="${instanceType.hardware.volumes}" var="volume">

									<tr>
										<td>
											${volume.device}
										</td>

										<td>
											${volume.size}
										</td>
										<td>
											${volume.bootDevice}
										</td>
										<td>
											${volume.durable}
										</td>
									</tr>

								</g:each>
							</table>
						</td>
						<%--<td class="cpu">${instanceType}</td>
            <td class="architecture">${instanceType}</td>
            <td class="ioPerformance">${instanceType}</td>
            <td><g:formatNumber number="${instanceType.hardware.linuxOnDemandPrice}" type="currency" currencyCode="USD" /></td>
            <td><g:formatNumber number="${instanceType.hardware.linuxReservedPrice}" type="currency" currencyCode="USD" /></td>
            <td><g:formatNumber number="${instanceType.hardware.linuxSpotPrice}" type="currency" currencyCode="USD" /></td>
            <td><g:formatNumber number="${instanceType.hardware.windowsOnDemandPrice}" type="currency" currencyCode="USD" /></td>
            <td><g:formatNumber number="${instanceType.hardware.windowsReservedPrice}" type="currency" currencyCode="USD" /></td>
            <td><g:formatNumber number="${instanceType.hardware.windowsSpotPrice}" type="currency" currencyCode="USD" /></td>
          --%>
					</tr>
				</g:each>
			</table>
    </div>
  </div>
</body>
</html>
