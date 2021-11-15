package mc.jabber.core.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class KnotClassWriter extends ClassWriter {
    public KnotClassWriter(int flags) {
        super(flags);
    }
    
    public KnotClassWriter(ClassReader classReader, int flags) {
        super(classReader, flags);
    }
}
