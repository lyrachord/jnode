/*
 * $Id$
 *
 * Copyright (C) 2003-2015 JNode.org
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; If not, write to the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
 
package org.jnode.fs.jfat;

import org.jnode.driver.Device;
import org.jnode.driver.block.FSBlockDeviceAPI;
import org.jnode.fs.BlockDeviceFileSystemType;
import org.jnode.fs.FileSystemException;
import org.jnode.partitions.PartitionTableEntry;


/**
 * @author gvt
 * @author Tango
 */
public class FatFileSystemType implements BlockDeviceFileSystemType<FatFileSystem> {
    public static final Class<FatFileSystemType> ID = FatFileSystemType.class;

    public String getName() {
        return "JFAT";
    }

    public boolean supports(PartitionTableEntry pte, byte[] firstSector, FSBlockDeviceAPI devApi) {
/*
        if (pte != null) {
            if (!pte.isValid())
                return false;

            if (!(pte instanceof IBMPartitionTableEntry))
                return false;

            final IBMPartitionTableEntry ipte = (IBMPartitionTableEntry) pte;

            final IBMPartitionTypes type = ipte.getSystemIndicator();
            if ((type == IBMPartitionTypes.PARTTYPE_WIN95_FAT32) ||
                    (type == IBMPartitionTypes.PARTTYPE_WIN95_FAT32_LBA)) {
                return true;
            } else {
                return false;
            }
        }
*/

        // Check for FAT-32
        if (firstSector[66] == 0x29 &&
            firstSector[82] == 'F' &&
            firstSector[83] == 'A' &&
            firstSector[84] == 'T' &&
            firstSector[85] == '3' &&
            firstSector[86] == '2') {
            return true;
        }

        // Check for FAT-16/12
        return (firstSector[38] == 0x29 &&
            firstSector[54] == 'F' &&
            firstSector[55] == 'A' &&
            firstSector[56] == 'T' &&
            firstSector[57] == '1' &&
            firstSector[58] == '2' || firstSector[58] == '6');
    }

    public FatFileSystem create(Device device, boolean readOnly) throws FileSystemException {
        return new FatFileSystem(device, readOnly, this);
    }

}
