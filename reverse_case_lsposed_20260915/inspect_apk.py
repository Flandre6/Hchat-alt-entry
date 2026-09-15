import argparse
import gc
import zipfile
from loguru import logger
logger.disable('androguard')
from androguard.core.dex import DEX

parser = argparse.ArgumentParser()
parser.add_argument('apk')
parser.add_argument('--anchors', nargs='*', default=[])
parser.add_argument('--classes', nargs='*', default=[])
parser.add_argument('--dex')
parser.add_argument('--methods', nargs='*', default=[])
args = parser.parse_args()
targets = {'L' + c.replace('.', '/').strip('L;') + ';' for c in args.classes}
with zipfile.ZipFile(args.apk) as archive:
    for name in archive.namelist():
        if not name.endswith('.dex') or (args.dex and name != args.dex):
            continue
        raw = archive.read(name)
        needles = args.anchors or list(targets)
        if needles and not any(s.encode() in raw for s in needles):
            continue
        print('DEX', name, flush=True)
        dex = DEX(raw)
        for cls in dex.get_classes():
            selected = cls.get_name() in targets
            hits = []
            if not targets:
                for method in cls.get_methods():
                    for ins in method.get_instructions():
                        if ins.get_name().startswith('const-string'):
                            out = ins.get_output()
                            if any(s in out for s in args.anchors):
                                hits.append((method.get_name(), method.get_descriptor(), out))
                selected = bool(hits)
            if not selected:
                continue
            print('CLASS', cls.get_name(), 'extends', cls.get_superclassname(), flush=True)
            for hit in hits:
                print('ANCHOR', *hit, flush=True)
            for field in cls.get_fields():
                print('FIELD', field.get_name(), field.get_descriptor())
            for method in cls.get_methods():
                print('METHOD', method.get_access_flags_string(), method.get_name(), method.get_descriptor())
                if args.methods and (method.get_name() in args.methods or '*' in args.methods):
                    for offset, ins in method.get_instructions_idx():
                        print(hex(offset), ins.get_name(), ins.get_output())
            print(flush=True)
        del dex, raw
        gc.collect()
