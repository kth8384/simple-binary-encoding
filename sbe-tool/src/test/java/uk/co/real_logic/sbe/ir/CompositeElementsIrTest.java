/*
 * Copyright 2015 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.real_logic.sbe.ir;

import org.junit.Test;
import uk.co.real_logic.sbe.PrimitiveType;
import uk.co.real_logic.sbe.xml.IrGenerator;
import uk.co.real_logic.sbe.xml.MessageSchema;
import uk.co.real_logic.sbe.xml.ParserOptions;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static uk.co.real_logic.sbe.TestUtil.getLocalResource;
import static uk.co.real_logic.sbe.xml.XmlSchemaParser.parse;

public class CompositeElementsIrTest
{
    @Test
    public void shouldGenerateCorrectIrForCompositeElementsSchema()
        throws Exception
    {
        final MessageSchema schema = parse(getLocalResource("composite-elements-schema.xml"), ParserOptions.DEFAULT);
        final IrGenerator irg = new IrGenerator();
        final Ir ir = irg.generate(schema);
        final List<Token> tokens = ir.getMessage(1);

        final Token fieldToken = tokens.get(1);
        final Token outerCompositeToken = tokens.get(2);
        final Token enumToken = tokens.get(3);
        final Token zerothToken = tokens.get(7);
        final Token setToken = tokens.get(8);
        final Token innerCompositeToken = tokens.get(13);
        final Token firstToken = tokens.get(14);
        final Token secondToken = tokens.get(15);

        assertThat(fieldToken.signal(), is(Signal.BEGIN_FIELD));
        assertThat(fieldToken.name(), is("structure"));

        assertThat(outerCompositeToken.signal(), is(Signal.BEGIN_COMPOSITE));
        assertThat(outerCompositeToken.name(), is("outer"));

        assertThat(enumToken.signal(), is(Signal.BEGIN_ENUM));
        assertThat(enumToken.name(), is("ENUM"));
        assertThat(enumToken.encodedLength(), is(1));
        assertThat(enumToken.encoding().primitiveType(), is(PrimitiveType.UINT8));
        assertThat(enumToken.offset(), is(0));

        assertThat(zerothToken.signal(), is(Signal.ENCODING));
        assertThat(zerothToken.offset(), is(1));
        assertThat(zerothToken.encoding().primitiveType(), is(PrimitiveType.UINT8));

        assertThat(setToken.signal(), is(Signal.BEGIN_SET));
        assertThat(setToken.name(), is("SET"));
        assertThat(setToken.encodedLength(), is(4));
        assertThat(setToken.encoding().primitiveType(), is(PrimitiveType.UINT32));
        assertThat(setToken.offset(), is(2));

        assertThat(innerCompositeToken.signal(), is(Signal.BEGIN_COMPOSITE));
        assertThat(innerCompositeToken.name(), is("inner"));
        assertThat(innerCompositeToken.offset(), is(6));

        assertThat(firstToken.signal(), is(Signal.ENCODING));
        assertThat(firstToken.name(), is("first"));
        assertThat(firstToken.offset(), is(0));
        assertThat(firstToken.encoding().primitiveType(), is(PrimitiveType.INT64));

        assertThat(secondToken.signal(), is(Signal.ENCODING));
        assertThat(secondToken.name(), is("second"));
        assertThat(secondToken.offset(), is(8));
        assertThat(secondToken.encoding().primitiveType(), is(PrimitiveType.INT64));
    }
}
