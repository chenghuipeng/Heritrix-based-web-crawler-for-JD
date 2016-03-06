/* $Id: Warc2Arc.java 4977 2007-03-09 23:57:28Z stack-sf $
 *
 * Created Aug 29, 2006
 *
 * Copyright (C) 2006 Internet Archive.
 *
 * This file is part of the Heritrix web crawler (crawler.archive.org).
 *
 * Heritrix is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * any later version.
 *
 * Heritrix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with Heritrix; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.archive.io;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.archive.io.arc.ARCWriter;
import org.archive.io.warc.WARCConstants;
import org.archive.io.warc.WARCReader;
import org.archive.io.warc.WARCReaderFactory;
import org.archive.io.warc.WARCRecord;
import org.archive.util.ArchiveUtils;
import org.archive.util.FileUtils;


/**
 * Convert WARCs to (sortof) ARCs.
 * WARCs can be 1Gig in size, that is, 10x default ARC size.  Script takes
 * directory as output and will write multiple ARCs for a single large WARC.
 * Only writes resource records of type <code>text/dns</code> or
 * <code>application/http; msgtype=response</code>.  All others -- metadata,
 * request -- are skipped.
 * @author stack
 * @version $Date: 2007-03-09 23:57:28 +0000 (Fri, 09 Mar 2007) $ $Revision: 4977 $
 */
public class Warc2Arc {
   private static void usage(HelpFormatter formatter, Options options,
           int exitCode) {
       formatter.printHelp("java org.archive.io.arc.Warc2Arc " +
       		"[--force] [--prefix=PREFIX] [--suffix=SUFFIX] WARC_INPUT " +
       		    "OUTPUT_DIR",
            options);
       System.exit(exitCode);
   }
   
   static String parseRevision(final String version) {
       final String ID = "$Revision: ";
       int index = version.indexOf(ID);
       return (index < 0)? version:
           version.substring(index + ID.length(), version.length() - 1).trim();
   }
   
   private static String getRevision() {
       return parseRevision("$Revision: 4977 $");
   }
   
   public void transform(final File warc, final File dir, final String prefix,
           final String suffix, final boolean force)
   throws IOException, java.text.ParseException {
       FileUtils.isReadable(warc);
       FileUtils.isReadable(dir);
       WARCReader reader = WARCReaderFactory.get(warc);
       List<String> metadata =  new ArrayList<String>();
       metadata.add("Made from " + reader.getReaderIdentifier() + " by " +
           this.getClass().getName() + "/" + getRevision());
       ARCWriter writer = new ARCWriter(new AtomicInteger(),
            Arrays.asList(new File [] {dir}), prefix, suffix,
            reader.isCompressed(), -1, metadata);
       transform(reader, writer);
   }

   protected void transform(final WARCReader reader, final ARCWriter writer)
   throws IOException, java.text.ParseException {
	   // No point digesting. Digest is available after reading of ARC which
	   // is too late for inclusion in WARC.
	   reader.setDigest(false);
       // I don't want the close being logged -- least, not w/o log of
       // an opening (and that'd be a little silly for simple script
       // like this). Currently, it logs at level INFO so that close
       // of files gets written to log files.  Up the log level just
       // for the close.
       Logger l = Logger.getLogger(writer.getClass().getName());
       Level oldLevel = l.getLevel();
	   try {
           l.setLevel(Level.WARNING);
		   for (final Iterator i = reader.iterator(); i.hasNext();) {
               WARCRecord r = (WARCRecord)i.next();
               if (!isARCType(r.getHeader().getMimetype())) {
                   continue;
               }
               if (r.getHeader().getContentBegin() <= 0) {
                   // Otherwise, because length include Header-Line and
                   // Named Fields, these will end up in the ARC unless there
                   // is a non-zero content begin.
                   continue;
               }
               String ip = (String)r.getHeader().
                   getHeaderValue((WARCConstants.HEADER_KEY_IP));
               long length = r.getHeader().getLength();
               int offset = r.getHeader().getContentBegin();
               // This mimetype is not exactly what you'd expect to find in
               // an ARC though technically its 'correct'.  To get right one,
               // need to parse the HTTP Headers.  Thats messy.  Not doing for
               // now.
               String mimetype = r.getHeader().getMimetype();
               // Clean out ISO time string '-', 'T', ':', and 'Z' characters.
               String t = r.getHeader().getDate().replaceAll("[-T:Z]", "");
               long time = ArchiveUtils.getSecondsSinceEpoch(t).getTime();
               writer.write(r.getHeader().getUrl(), mimetype, ip, time,
                   (int)(length - offset), r);
		   }
	   } finally {
		   if (reader != null) {
			   reader.close();
		   }
		   if (writer != null) {
			   try {
				   writer.close();
			   } finally {
				   l.setLevel(oldLevel);
			   }
		   }
	   }
   }
   
   protected boolean isARCType(final String mimetype) {
       // Comparing mimetypes, especially WARC types can be problematic since
       // they have whitespace.  For now, ignore.
       if (mimetype == null || mimetype.length() <= 0) {
           return false;
       }
       String cleaned = mimetype.toLowerCase().trim();
       if (cleaned.equals(WARCConstants.HTTP_RESPONSE_MIMETYPE) ||
               cleaned.equals("text/dns")) {
           return true;
       }
       return false;
   }

   /**
    * Command-line interface to Arc2Warc.
    *
    * @param args Command-line arguments.
    * @throws ParseException Failed parse of the command line.
    * @throws IOException
    * @throws java.text.ParseException
    */
   public static void main(String [] args)
   throws ParseException, IOException, java.text.ParseException {
       Options options = new Options();
       options.addOption(new Option("h","help", false,
           "Prints this message and exits."));
       options.addOption(new Option("f","force", false,
       	   "Force overwrite of target file."));
       options.addOption(new Option("p","prefix", true,
           "Prefix to use on created ARC files, else uses default."));
       options.addOption(new Option("s","suffix", true,
           "Suffix to use on created ARC files, else uses default."));
       PosixParser parser = new PosixParser();
       CommandLine cmdline = parser.parse(options, args, false);
       List cmdlineArgs = cmdline.getArgList();
       Option [] cmdlineOptions = cmdline.getOptions();
       HelpFormatter formatter = new HelpFormatter();
       
       // If no args, print help.
       if (cmdlineArgs.size() < 0) {
           usage(formatter, options, 0);
       }

       // Now look at options passed.
       boolean force = false;
       String prefix = "WARC2ARC";
       String suffix = null;
       for (int i = 0; i < cmdlineOptions.length; i++) {
           switch(cmdlineOptions[i].getId()) {
               case 'h':
                   usage(formatter, options, 0);
                   break;
                   
               case 'f':
                   force = true;
                   break;
                   
               case 'p':
                   prefix = cmdlineOptions[i].getValue();
                   break;
                   
               case 's':
                   suffix = cmdlineOptions[i].getValue();
                   break;
                   
               default:
                   throw new RuntimeException("Unexpected option: " +
                       + cmdlineOptions[i].getId());
           }
       }
       
       // If no args, print help.
       if (cmdlineArgs.size() != 2) {
           usage(formatter, options, 0);
       }
       (new Warc2Arc()).transform(new File(cmdlineArgs.get(0).toString()),
           new File(cmdlineArgs.get(1).toString()), prefix, suffix, force);
   }
}