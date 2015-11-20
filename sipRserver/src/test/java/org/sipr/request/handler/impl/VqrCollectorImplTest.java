package org.sipr.request.handler.impl;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class VqrCollectorImplTest {

    VqrCollectorImpl collector = new VqrCollectorImpl();

    @Test
    public void testProcessVQIntervalReport() throws Exception {

        String vqIntervalReport = "VQIntervalReport\n" +
                "LocalMetrics:\n" +
                "TimeStamps:START=2015-11-20T10:59:33Z STOP=2015-11-20T11:03:12Z\n" +
                "SessionDesc:PT=9 PPS=50 SSUP=off\n" +
                "CallID:785c72a8-8c64a097-761b4eec@10.2.0.52\n" +
                "ToID:\"Cristian Moisa\" <sip:2087@1410setup2.cristi.ezuce.ro>\n" +
                "FromID:\"George Niculae\" <sip:2012@1410setup2.cristi.ezuce.ro>\n" +
                "LocalAddr:IP=10.2.0.51 PORT=2270 SSRC=4209273062\n" +
                "RemoteAddr:IP=10.2.0.52 PORT=2298 SSRC=3536961311\n" +
                "JitterBuffer:JBA=3 JBR=5 JBN=220 JBM=230 JBX=160\n" +
                "PacketLoss:NLR=0.0 JDR=0.0\n" +
                "BurstGapLoss:BLD=0.0 BD=0 GLD=0.0 GD=65535 GMIN=16\n" +
                "Delay:RTD=3 ESD=239 OWD=240 IAJ=0\n" +
                "Signal:RERL=127\n" +
                "QualityEst:RLQ=94 RCQ=83 MOSLQ=3.8 MOSCQ=3.4\n" +
                "DialogID:785c72a8-8c64a097-761b4eec@10.2.0.52;to-tag=9F03E140-1A25979F;from-tag=270B864E-AD6B403C";

        String processedContent = collector.processContent(vqIntervalReport.getBytes(StandardCharsets.UTF_8));
        assertEquals("ReportType:VQIntervalReport,LocalMetrics:,TimeStamps:START=2015-11-20T10:59:33Z STOP=2015-11-20T11:03:12Z," +
                "SessionDesc:PT=9 PPS=50 SSUP=off,CallID:785c72a8-8c64a097-761b4eec@10.2.0.52," +
                "ToID:Cristian Moisa <sip:2087@1410setup2.cristi.ezuce.ro>,FromID:George Niculae <sip:2012@1410setup2.cristi.ezuce.ro>," +
                "LocalAddr:IP=10.2.0.51 PORT=2270 SSRC=4209273062,RemoteAddr:IP=10.2.0.52 PORT=2298 SSRC=3536961311," +
                "JitterBuffer:JBA=3 JBR=5 JBN=220 JBM=230 JBX=160,PacketLoss:NLR=0.0 JDR=0.0," +
                "BurstGapLoss:BLD=0.0 BD=0 GLD=0.0 GD=65535 GMIN=16,Delay:RTD=3 ESD=239 OWD=240 IAJ=0," +
                "Signal:RERL=127,QualityEst:RLQ=94 RCQ=83 MOSLQ=3.8 MOSCQ=3.4," +
                "DialogID:785c72a8-8c64a097-761b4eec@10.2.0.52;to-tag=9F03E140-1A25979F;from-tag=270B864E-AD6B403C", processedContent);

    }

    @Test
    public void testProcessVQAlertReport() throws Exception {

        String vqIntervalReport = "VQAlertReport\n" +
                "Type=OWD Severity=Critical Dir=local\n" +
                "Metrics:\n" +
                "TimeStamps:START=2015-11-20T08:43:05Z STOP=2015-11-20T08:43:14Z\n" +
                "SessionDesc:PT=9 PPS=50 SSUP=off\n" +
                "CallID:41a24694-78e60cfb-69fb60f8@10.2.0.52\n" +
                "ToID:\"Cristian Moisa\" <sip:2087@1410setup2.cristi.ezuce.ro>\n" +
                "FromID:\"George Niculae\" <sip:2012@1410setup2.cristi.ezuce.ro>\n" +
                "LocalAddr:IP=10.2.0.51 PORT=2302 SSRC=3825483318\n" +
                "RemoteAddr:IP=10.2.0.52 PORT=2274 SSRC=2017446972\n" +
                "JitterBuffer:JBA=3 JBR=5 JBN=220 JBM=230 JBX=160\n" +
                "PacketLoss:NLR=0.0 JDR=0.0\n" +
                "BurstGapLoss:BLD=0.0 BD=0 GLD=0.0 GD=9440 GMIN=16\n" +
                "Delay:RTD=2 ESD=239 OWD=240 IAJ=0\n" +
                "Signal:RERL=127\n" +
                "QualityEst:RLQ=94 RCQ=82 MOSLQ=3.8 MOSCQ=3.4\n" +
                "DialogID:41a24694-78e60cfb-69fb60f8@10.2.0.52;to-tag=1C1C51EC-929AF443;from-tag=DCC76934-527D72DE";

        String processedContent = collector.processContent(vqIntervalReport.getBytes(StandardCharsets.UTF_8));
        assertEquals("ReportType:VQAlertReport,Type=OWD Severity=Critical Dir=local,Metrics:,TimeStamps:START=2015-11-20T08:43:05Z STOP=2015-11-20T08:43:14Z," +
                "SessionDesc:PT=9 PPS=50 SSUP=off,CallID:41a24694-78e60cfb-69fb60f8@10.2.0.52," +
                "ToID:Cristian Moisa <sip:2087@1410setup2.cristi.ezuce.ro>,FromID:George Niculae <sip:2012@1410setup2.cristi.ezuce.ro>," +
                "LocalAddr:IP=10.2.0.51 PORT=2302 SSRC=3825483318,RemoteAddr:IP=10.2.0.52 PORT=2274 SSRC=2017446972," +
                "JitterBuffer:JBA=3 JBR=5 JBN=220 JBM=230 JBX=160,PacketLoss:NLR=0.0 JDR=0.0,BurstGapLoss:BLD=0.0 BD=0 GLD=0.0 GD=9440 GMIN=16," +
                "Delay:RTD=2 ESD=239 OWD=240 IAJ=0,Signal:RERL=127,QualityEst:RLQ=94 RCQ=82 MOSLQ=3.8 MOSCQ=3.4," +
                "DialogID:41a24694-78e60cfb-69fb60f8@10.2.0.52;to-tag=1C1C51EC-929AF443;from-tag=DCC76934-527D72DE", processedContent);

    }

    @Test
    public void testProcessVQSesionReport() throws Exception {

        String vqIntervalReport = "VQSessionReport\n" +
                "LocalMetrics:\n" +
                "TimeStamps:START=2015-11-20T08:43:05Z STOP=2015-11-20T08:46:30Z\n" +
                "SessionDesc:PT=9 PPS=50 SSUP=off\n" +
                "CallID:41a24694-78e60cfb-69fb60f8@10.2.0.52\n" +
                "ToID:\"Cristian Moisa\" <sip:2087@1410setup2.cristi.ezuce.ro>\n" +
                "FromID:\"George Niculae\" <sip:2012@1410setup2.cristi.ezuce.ro>\n" +
                "LocalAddr:IP=10.2.0.51 PORT=2302 SSRC=3825483318\n" +
                "RemoteAddr:IP=10.2.0.52 PORT=2274 SSRC=2017446972\n" +
                "JitterBuffer:JBA=3 JBR=5 JBN=220 JBM=230 JBX=160\n" +
                "PacketLoss:NLR=0.0 JDR=0.0\n" +
                "BurstGapLoss:BLD=0.0 BD=0 GLD=0.0 GD=65535 GMIN=16\n" +
                "Delay:RTD=3 ESD=239 OWD=240 IAJ=0\n" +
                "Signal:RERL=127\n" +
                "QualityEst:RLQ=94 RCQ=82 MOSLQ=3.8 MOSCQ=3.4\n" +
                "DialogID:41a24694-78e60cfb-69fb60f8@10.2.0.52;to-tag=1C1C51EC-929AF443;from-tag=DCC76934-527D72DE";

        String processedContent = collector.processContent(vqIntervalReport.getBytes(StandardCharsets.UTF_8));
        assertEquals("ReportType:VQSessionReport,LocalMetrics:,TimeStamps:START=2015-11-20T08:43:05Z STOP=2015-11-20T08:46:30Z," +
                "SessionDesc:PT=9 PPS=50 SSUP=off,CallID:41a24694-78e60cfb-69fb60f8@10.2.0.52," +
                "ToID:Cristian Moisa <sip:2087@1410setup2.cristi.ezuce.ro>,FromID:George Niculae <sip:2012@1410setup2.cristi.ezuce.ro>," +
                "LocalAddr:IP=10.2.0.51 PORT=2302 SSRC=3825483318,RemoteAddr:IP=10.2.0.52 PORT=2274 SSRC=2017446972," +
                "JitterBuffer:JBA=3 JBR=5 JBN=220 JBM=230 JBX=160,PacketLoss:NLR=0.0 JDR=0.0,BurstGapLoss:BLD=0.0 BD=0 GLD=0.0 GD=65535 GMIN=16," +
                "Delay:RTD=3 ESD=239 OWD=240 IAJ=0,Signal:RERL=127,QualityEst:RLQ=94 RCQ=82 MOSLQ=3.8 MOSCQ=3.4," +
                "DialogID:41a24694-78e60cfb-69fb60f8@10.2.0.52;to-tag=1C1C51EC-929AF443;from-tag=DCC76934-527D72DE", processedContent);

    }

    @Test
    public void testGetEventType() throws Exception {
        assertEquals("vq-rtcpxr", collector.getEventType());
    }
}